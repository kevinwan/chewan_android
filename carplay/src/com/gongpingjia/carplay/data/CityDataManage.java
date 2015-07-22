package com.gongpingjia.carplay.data;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.gongpingjia.carplay.CarPlayApplication;
import com.gongpingjia.carplay.bean.CityModel;
import com.gongpingjia.carplay.bean.DistrictModel;
import com.gongpingjia.carplay.bean.ProvinceModel;
import com.gongpingjia.carplay.service.XmlParserHandler;

import android.content.res.AssetManager;

public class CityDataManage
{
    
    public static CityDataManage instance;
    
    public static CityDataManage getInstance()
    {
        if (instance == null)
        {
            instance = new CityDataManage();
        }
        
        return instance;
    }
    
    /**
     * 所有省
     */
    public static String[] mProvinceDatas;
    
    /**
     * key - 省 value - 市
     */
    public static Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    
    /**
     * key - 市 values - 区
     */
    public static Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    
    /**
     * key - 区 values - 邮编
     */
    public static Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    
    /**
     * 当前省的名称
     */
    public static String mCurrentProviceName;
    
    /**
     * 当前市的名称
     */
    public static String mCurrentCityName;
    
    /**
     * 当前区的名称
     */
    public static String mCurrentDistrictName = "";
    
    /**
     * 当前区的邮政编码
     */
    public static String mCurrentZipCode = "";
    
    /**
     * 解析省市区的XML数据
     */
    
    public static void initProvinceDatas()
    {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = CarPlayApplication.getInstance().getAssets();
        try
        {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            // */ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty())
            {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty())
                {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            // */
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++)
            {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++)
                {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++)
                    {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel =
                            new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        finally
        {
            
        }
    }
    
    public String[] getmProvinceDatas()
    {
        return mProvinceDatas;
    }
    
    public void setmProvinceDatas(String[] mProvinceDatas)
    {
        CityDataManage.mProvinceDatas = mProvinceDatas;
    }
    
    public Map<String, String[]> getmCitisDatasMap()
    {
        return mCitisDatasMap;
    }
    
    public void setmCitisDatasMap(Map<String, String[]> mCitisDatasMap)
    {
        CityDataManage.mCitisDatasMap = mCitisDatasMap;
    }
    
    public Map<String, String[]> getmDistrictDatasMap()
    {
        return mDistrictDatasMap;
    }
    
    public void setmDistrictDatasMap(Map<String, String[]> mDistrictDatasMap)
    {
        CityDataManage.mDistrictDatasMap = mDistrictDatasMap;
    }
    
    public String getmCurrentProviceName()
    {
        return mCurrentProviceName;
    }
    
    public void setmCurrentProviceName(String mCurrentProviceName)
    {
        CityDataManage.mCurrentProviceName = mCurrentProviceName;
    }
    
    public String getmCurrentCityName()
    {
        return mCurrentCityName;
    }
    
    public void setmCurrentCityName(String mCurrentCityName)
    {
        CityDataManage.mCurrentCityName = mCurrentCityName;
    }
    
    public String getmCurrentDistrictName()
    {
        return mCurrentDistrictName;
    }
    
    public void setmCurrentDistrictName(String mCurrentDistrictName)
    {
        CityDataManage.mCurrentDistrictName = mCurrentDistrictName;
    }
    
}
