package com.gongpingjia.carplay.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gongpingjia.carplay.CarPlayValueFix;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.bean.CarBrand;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @Description 车辆型号
 * @author Administrator
 * @date 2015-7-21 上午10:49:07
 */
public class BrandAdapter extends BaseAdapter implements SectionIndexer {

    private List<CarBrand> mDatas;

    private LayoutInflater mInflater;

    private Context mContext;

    public BrandAdapter(Context context, List<CarBrand> datas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDatas.size();
    }

    @Override
    public CarBrand getItem(int position) {
        // TODO Auto-generated method stub
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        boolean letterVisible = true;
        CarBrand brand = mDatas.get(position);
        if (position != 0) {
            CarBrand preBrand = mDatas.get(position - 1);
            if (brand.getFirstLetter().equals(preBrand.getFirstLetter())) {
                // 和前一个车牌字母相同
                letterVisible = false;
            }
        }

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_car_brand, null);
            holder = new ViewHolder();
            holder.brandText = (TextView) convertView.findViewById(R.id.tv_brand_name);
            holder.letterText = (TextView) convertView.findViewById(R.id.tv_letter);
            holder.logImg = (ImageView) convertView.findViewById(R.id.imgView_car_logo);
            holder.logImg.setTag(brand.getUrl());
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.brandText.setText(brand.getBrand());
        if (letterVisible) {
            holder.letterText.setVisibility(View.VISIBLE);
            holder.letterText.setText(brand.getFirstLetter());
        } else {
            holder.letterText.setVisibility(View.GONE);
        }
        // url不一致
        ImageLoader.getInstance().displayImage(brand.getUrl(), holder.logImg, CarPlayValueFix.optionsDefault);
        return convertView;
    }

    class ViewHolder {
        TextView letterText;

        ImageView logImg;

        TextView brandText;
    }

    @Override
    public Object[] getSections() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        // TODO Auto-generated method stub
        String str = Character.toString((char) sectionIndex);
        for (CarBrand brand : mDatas) {
            if (brand.getFirstLetter().equals(str)) {
                return mDatas.indexOf(brand);
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

}
