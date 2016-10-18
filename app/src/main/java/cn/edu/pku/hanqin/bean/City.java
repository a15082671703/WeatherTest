package cn.edu.pku.hanqin.bean;

/**
 * Created by 76568 on 2016/10/18 0018.
 */
public class City {
    private String province;
    private String city;
    private String number;
    private String firstPY;
    private String allPY;
    private String allFristPY;
    public City(String province,String city , String number , String firstPY , String allPY , String allFristPY){
        this.province = province;
        this.city = city;
        this.number = number;
        this.firstPY = firstPY;
        this.allFristPY = allFristPY;
        this.allPY = allPY;
    }
}
