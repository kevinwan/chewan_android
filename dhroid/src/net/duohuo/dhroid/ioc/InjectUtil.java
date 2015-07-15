package net.duohuo.dhroid.ioc;

import java.lang.reflect.Field;

import net.duohuo.dhroid.ioc.annotation.Inject;
import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.ioc.annotation.InjectView;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

/**
 * 注入工具类
 * 
 * @author duohuo-jinghao
 * 
 */
@SuppressWarnings("rawtypes")
public class InjectUtil {

	public static final String LOG_TAG = "duohuo_InjectUtil";

	/**
	 * 在activity中注入 在activity setContext 后调用 其他的类中可以初始化时可调用
	 * 
	 * @param activity
	 * @param layoutResId
	 */
	public static void inject(Object obj) {

		Field[] fields = obj.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				field.setAccessible(true);
				try {
					if (field.get(obj) != null)
						continue;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				// 注入view
				InjectView viewInject = field.getAnnotation(InjectView.class);
				if (viewInject != null) {
					indectView(obj, field, viewInject);
				}
				// 标准ioc注入
				Inject inject = field.getAnnotation(Inject.class);
				if (inject != null) {
					injectStand(obj, field, inject);
				}

				if (obj instanceof Activity||obj instanceof Fragment) {
					// extra
					InjectExtra extra = field.getAnnotation(InjectExtra.class);
					if (extra != null) {
						getExtras(obj, field, extra);
					}
				}
			}

		}
	}

	public static void unInjectView(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				field.setAccessible(true);
				// 注入view
				InjectView viewInject = field.getAnnotation(InjectView.class);
				if (viewInject != null) {
					try {
						field.set(obj, null);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 注入
	 * 
	 * @param obj
	 * @param field
	 * @param injectview
	 */
	public static void indectView(Object obj, Field field, InjectView injectview) {
		// view
		InjectView viewInject = field.getAnnotation(InjectView.class);
		View view = null;

		int layout = viewInject.layout();
		// layout中获取
		if (layout != 0) {
			view = LayoutInflater.from(
					IocContainer.getShare().getApplicationContext()).inflate(
					layout, null);
		} else {
			// 在其他view中的view
			String inView = viewInject.inView();
			if (!TextUtils.isEmpty(inView)) {
				try {
					Field inViewField = obj.getClass().getDeclaredField(inView);
					inViewField.setAccessible(true);
					View parentView = (View) inViewField.get(obj);
					if (parentView == null) {
						indectView(obj, inViewField,
								inViewField.getAnnotation(InjectView.class));
						parentView = (View) inViewField.get(obj);
					}
					view = parentView.findViewById(viewInject.id());
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				// 在activity中的view
				if (obj instanceof Activity) {
					Activity act = (Activity) obj;
					view = act.findViewById(viewInject.id());
				}
				if (obj instanceof Dialog) {
					Dialog act = (Dialog) obj;
					view = act.findViewById(viewInject.id());
				}
				if (obj instanceof Fragment) {
					Fragment act = (Fragment) obj;
					view = act.getView().findViewById(viewInject.id());
				} else if (obj instanceof View) {
					View vtemp = (View) obj;
					view = vtemp.findViewById(viewInject.id());
				} else if (obj instanceof ContentView) {
					ContentView vtemp = (ContentView) obj;
					view = vtemp.getContentView().findViewById(viewInject.id());
				}
			}
		}

		try {
			field.set(obj, view);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// 事件绑定
		String clickMethod = viewInject.click();
		if (!TextUtils.isEmpty(clickMethod))
			setViewClickListener(obj, field, clickMethod);

		String longClickMethod = viewInject.longClick();
		if (!TextUtils.isEmpty(longClickMethod))
			setViewLongClickListener(obj, field, longClickMethod);

		String itemClickMethod = viewInject.itemClick();
		if (!TextUtils.isEmpty(itemClickMethod))
			setItemClickListener(obj, field, itemClickMethod);

		String itemLongClickMethod = viewInject.itemLongClick();
		if (!TextUtils.isEmpty(itemLongClickMethod))
			setItemLongClickListener(obj, field, itemLongClickMethod);

	}

	/**
	 * 注入ioc容器中的对象
	 * 
	 * @param obj
	 * @param field
	 * @param inject
	 */
	@SuppressWarnings({ "unchecked" })
	public static void injectStand(Object obj, Field field, Inject inject) {
		try {

			String name = inject.name();
			Object value = null;
			if (!TextUtils.isEmpty(name)) {
				value = IocContainer.getShare().get(name);

			} else {
				Class clazz = field.getType();
				String tag = inject.tag();
				if (!TextUtils.isEmpty(tag)) {
					value = IocContainer.getShare().get(clazz, tag);
				} else {
					value = IocContainer.getShare().get(clazz);
				}
			}
			field.set(obj, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void getExtras(Object activity, Field field, InjectExtra extra) {
		Bundle bundle = null;
		if (activity instanceof Activity) {
			Activity ac = (Activity) activity;
			bundle = ac.getIntent().getExtras();
		} else if (activity instanceof Fragment) {
			Fragment fg = (Fragment) activity;
			bundle = fg.getArguments();
		}
		if(bundle==null) return;
		
		try {
			Object obj = null;
			Class clazz = field.getType();
			if (clazz.equals(Integer.class)) {
				if (!TextUtils.isEmpty(extra.def())) {
					obj = bundle.getInt(extra.name(),
							Integer.parseInt(extra.def()));
				} else {
					obj = bundle.getInt(extra.name(), 0);
				}
			} else if (clazz.equals(String.class)) {
				obj = bundle.getString(extra.name());
				if (obj == null) {
					if (!TextUtils.isEmpty(extra.def())) {
						obj = extra.def();
					}
				}
			} else if (clazz.equals(Long.class)) {
				if (!TextUtils.isEmpty(extra.def())) {
					obj = bundle.getLong(extra.name(),
							Long.parseLong(extra.def()));
				} else {
					obj = bundle.getLong(extra.name(), 0);
				}
			} else if (clazz.equals(Float.class)) {
				if (!TextUtils.isEmpty(extra.def())) {
					obj = bundle.getFloat(extra.name(),
							Float.parseFloat(extra.def()));
				} else {
					obj = bundle.getFloat(extra.name(), 0);
				}
			} else if (clazz.equals(Boolean.class)) {
				if (!TextUtils.isEmpty(extra.def())) {
					obj = bundle.getBoolean(extra.name(),
							Boolean.parseBoolean(extra.def()));
				} else {
					obj = bundle.getBoolean(extra.name(), true);
				}

			} else if ((clazz.equals(JSONObject.class))) {
				String objstr = bundle.getString(extra.name());
				if (!TextUtils.isEmpty(objstr)) {
					obj = new JSONObject(objstr);
				}
			} else if (clazz.equals(JSONArray.class)) {
				String objstr = bundle.getString(extra.name());
				if (!TextUtils.isEmpty(objstr)) {
					obj = new JSONArray(objstr);
				}
			} else {
				String objstr = bundle.getString(extra.name());
				if (!TextUtils.isEmpty(objstr)) {
					try {
						obj = new Gson().fromJson(objstr, clazz);
					} catch (Exception e) {
					}
				}
			}
			if (obj != null) {
				field.set(activity, obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /***
	// * 在其他视图中注入
	// * @param activity
	// * @param view
	// */
	// public static void injectView(Object activity, View view) {
	// Field[] fields = activity.getClass().getDeclaredFields();
	// if (fields != null && fields.length > 0) {
	// for (Field field : fields) {
	// ViewInject viewInject = field.getAnnotation(ViewInject.class);
	// if (viewInject != null) {
	// int viewId = viewInject.id();
	// try {
	// field.setAccessible(true);
	// field.set(activity, view.findViewById(viewId));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// String clickMethod = viewInject.click();
	// if (!TextUtils.isEmpty(clickMethod))
	// setViewClickListener(activity, field, clickMethod);
	//
	// String longClickMethod = viewInject.click();
	// if (!TextUtils.isEmpty(longClickMethod))
	// setViewLongClickListener(activity, field,
	// longClickMethod);
	// String itemClickMethod = viewInject.itemClick();
	// if (!TextUtils.isEmpty(itemClickMethod))
	// setItemClickListener(activity, field, itemClickMethod);
	//
	// String itemLongClickMethod = viewInject.itemClick();
	// if (!TextUtils.isEmpty(itemLongClickMethod))
	// setItemLongClickListener(activity, field,
	// itemLongClickMethod);
	//
	// Select select = viewInject.select();
	// if (!TextUtils.isEmpty(select.selected()))
	// setViewSelectListener(activity, field,
	// select.selected(), select.noSelected());
	//
	// }
	// }
	// }
	// }

	private static void setViewClickListener(Object activity, Field field,
			String clickMethod) {
		try {
			Object obj = field.get(activity);
			if (obj instanceof View) {
				((View) obj).setOnClickListener(new EventListener(activity)
						.click(clickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setViewLongClickListener(Object activity, Field field,
			String clickMethod) {
		try {
			Object obj = field.get(activity);
			if (obj instanceof View) {
				((View) obj).setOnLongClickListener(new EventListener(activity)
						.longClick(clickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setItemClickListener(Object activity, Field field,
			String itemClickMethod) {
		try {
			Object obj = field.get(activity);
			if (obj instanceof AdapterView) {
				((AdapterView) obj).setOnItemClickListener(new EventListener(
						activity).itemClick(itemClickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setItemLongClickListener(Object activity, Field field,
			String itemClickMethod) {
		try {
			Object obj = field.get(activity);
			if (obj instanceof AdapterView) {
				((AdapterView) obj)
						.setOnItemLongClickListener(new EventListener(activity)
								.itemLongClick(itemClickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
