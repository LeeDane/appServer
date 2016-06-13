package com.cn.leedane.arithmetic;

import java.util.ArrayList;
import java.util.List;
public class InitData {

	public static GZMetro init(){ 
		
		GZMetro gzMetro = new GZMetro();
		List<MetroLine> lines = new ArrayList<MetroLine>();
		lines.add(getLine1());
		lines.add(getLine2());
		lines.add(getLine3());
		lines.add(getLine30());
		
		return gzMetro;
	}
	
	//获得一号线的数据
	public static MetroLine getLine1(){
		MetroLine line1 = new MetroLine();
		List<Station> stations =new ArrayList<Station>();
		
		Station station101 = new Station();
		station101.setId(101);
		station101.setName("广州东站");
		stations.add(station101);
		
		Station station102 = new Station();
		station102.setId(102);
		station102.setName("体育中心");
		stations.add(station102);
		
		Station station103 = new Station();
		station103.setId(103);
		station103.setName("体育西路");
		stations.add(station103);
		
		Station station104 = new Station();
		station104.setId(104);
		station104.setName("杨箕");
		stations.add(station104);
		
		Station station105 = new Station();
		station105.setId(105);
		station105.setName("东山口");
		stations.add(station105);
		
		Station station106 = new Station();
		station106.setId(106);
		station106.setName("烈士陵园");
		stations.add(station106);
		
		Station station107 = new Station();
		station107.setId(107);
		station107.setName("农讲所");
		stations.add(station107);
		
		Station station108 = new Station();
		station108.setId(108);
		station108.setName("公园前");
		stations.add(station108);
		
		Station station109 = new Station();
		station109.setId(109);
		station109.setName("西门口");
		stations.add(station109);
		
		Station station110 = new Station();
		station110.setId(110);
		station110.setName("陈家祠");
		stations.add(station110);
		
		Station station111 = new Station();
		station111.setId(111);
		station111.setName("长寿路");
		stations.add(station111);
		
		Station station112 = new Station();
		station112.setId(112);
		station112.setName("黄沙");
		stations.add(station112);
		
		Station station113 = new Station();
		station113.setId(113);
		station113.setName("芳村");
		stations.add(station113);
		
		Station station114 = new Station();
		station114.setId(114);
		station114.setName("花地湾");
		stations.add(station114);
		
		Station station115 = new Station();
		station115.setId(115);
		station115.setName("坑口");
		stations.add(station115);
		
		Station station116 = new Station();
		station116.setId(116);
		station116.setName("西朗");
		stations.add(station116);
		
		line1.setId(1);
		line1.setName("1号线");
		line1.setStation(stations);
		return line1;
	}
	
	//获得二号线的数据
	public static MetroLine getLine2(){
		MetroLine line2 = new MetroLine();
		List<Station> stations =new ArrayList<Station>();
		
		Station station201 = new Station();
		station201.setId(201);
		station201.setName("嘉禾望岗站");
		stations.add(station201);
		
		Station station202 = new Station();
		station202.setId(202);
		station202.setName("黄边");
		stations.add(station202);
		
		Station station203 = new Station();
		station203.setId(203);
		station203.setName("江夏");
		stations.add(station203);
		
		Station station204 = new Station();
		station204.setId(204);
		station204.setName("萧岗");
		stations.add(station204);
		
		Station station205 = new Station();
		station205.setId(205);
		station205.setName("白云文化广场");
		stations.add(station205);
		
		Station station206 = new Station();
		station206.setId(206);
		station206.setName("白云公园");
		stations.add(station206);
		
		Station station207 = new Station();
		station207.setId(207);
		station207.setName("飞翔公园");
		stations.add(station207);
		
		Station station208 = new Station();
		station208.setId(208);
		station208.setName("三元里");
		stations.add(station208);
		
		Station station209 = new Station();
		station209.setId(209);
		station209.setName("广州火车站");
		stations.add(station209);
		
		Station station210 = new Station();
		station210.setId(210);
		station210.setName("越秀公园");
		stations.add(station210);
		
		Station station211 = new Station();
		station211.setId(211);
		station211.setName("纪念堂");
		stations.add(station211);
		
		Station station212 = new Station();
		station212.setId(212);
		station212.setName("公园前");
		stations.add(station212);
		
		Station station213 = new Station();
		station213.setId(213);
		station213.setName("海珠广场");
		stations.add(station213);
		
		Station station214 = new Station();
		station214.setId(214);
		station214.setName("市二宫");
		stations.add(station214);
		
		Station station215 = new Station();
		station215.setId(215);
		station215.setName("江南西");
		stations.add(station215);
		
		Station station216 = new Station();
		station216.setId(216);
		station216.setName("昌岗");
		stations.add(station216); 
		
		Station station217 = new Station();
		station217.setId(217);
		station217.setName("江泰路");
		stations.add(station217);


		Station station218 = new Station();
		station218.setId(218);
		station218.setName("东晓南");
		stations.add(station218);
		
		Station station219 = new Station();
		station219.setId(219);
		station219.setName("南洲");
		stations.add(station219);
		
		Station station220 = new Station();
		station220.setId(220);
		station220.setName("洛溪");
		stations.add(station220);
		
		Station station221 = new Station();
		station221.setId(221);
		station221.setName("南浦");
		stations.add(station221);
		
		Station station222 = new Station();
		station222.setId(222);
		station222.setName("会江");
		stations.add(station222);
		
		Station station223 = new Station();
		station223.setId(223);
		station223.setName("石壁");
		stations.add(station223);
		
		Station station224 = new Station();
		station224.setId(224);
		station224.setName("广州南站");
		stations.add(station224);
		
		line2.setId(2);
		line2.setName("2号线");
		line2.setStation(stations);
		return line2;
	}
	
	//获得三号线的数据
	public static MetroLine getLine3(){
		MetroLine line3 = new MetroLine();
		List<Station> stations =new ArrayList<Station>();
		
		Station station301 = new Station();
		station301.setId(301);
		station301.setName("天河客运站");
		stations.add(station301);
		
		Station station302 = new Station();
		station302.setId(302);
		station302.setName("五山");
		stations.add(station302);
		
		Station station303 = new Station();
		station303.setId(303);
		station303.setName("华师");
		stations.add(station303);
		
		Station station304 = new Station();
		station304.setId(304);
		station304.setName("岗顶");
		stations.add(station304);
		
		Station station305 = new Station();
		station305.setId(305);
		station305.setName("石牌桥");
		stations.add(station305);
		
		Station station306 = new Station();
		station306.setId(306);
		station306.setName("体育西路");
		stations.add(station306);
		
		Station station307 = new Station();
		station307.setId(307);
		station307.setName("珠江新城");
		stations.add(station307);
		
		Station station308 = new Station();
		station308.setId(308);
		station308.setName("广州塔");
		stations.add(station308);
		
		Station station309 = new Station();
		station309.setId(309);
		station309.setName("客村");
		stations.add(station309);
		
		Station station310 = new Station();
		station310.setId(310);
		station310.setName("大塘");
		stations.add(station310);
		
		Station station311 = new Station();
		station311.setId(311);
		station311.setName("沥滘");
		stations.add(station311);
		
		Station station312 = new Station();
		station312.setId(312);
		station312.setName("厦滘");
		stations.add(station312);
		
		Station station313 = new Station();
		station313.setId(313);
		station313.setName("大石");
		stations.add(station313);
		
		Station station314 = new Station();
		station314.setId(314);
		station314.setName("汉溪长隆");
		stations.add(station314);
		
		Station station315 = new Station();
		station315.setId(315);
		station315.setName("市桥");
		stations.add(station315);
		
		Station station316 = new Station();
		station316.setId(316);
		station316.setName("昌岗");
		stations.add(station316); 
		
		Station station317 = new Station();
		station317.setId(317);
		station317.setName("番禺广场");
		stations.add(station317);


		line3.setId(3);
		line3.setName("3号线");
		line3.setStation(stations);
		return line3;
	}
	
	//获得三号线北延线的数据
	public static MetroLine getLine30(){
		MetroLine line30 = new MetroLine();
		List<Station> stations =new ArrayList<Station>();
		
		Station station3001 = new Station();
		station3001.setId(3001);
		station3001.setName("机场南");
		stations.add(station3001);
		
		Station station3002 = new Station();
		station3002.setId(3002);
		station3002.setName("人和");
		stations.add(station3002);
		
		Station station3003 = new Station();
		station3003.setId(3003);
		station3003.setName("龙归");
		stations.add(station3003);
		
		Station station3004 = new Station();
		station3004.setId(3004);
		station3004.setName("嘉禾望岗");
		stations.add(station3004);
		
		Station station3005 = new Station();
		station3005.setId(3005);
		station3005.setName("白云大道北");
		stations.add(station3005);
		
		Station station3006 = new Station();
		station3006.setId(3006);
		station3006.setName("永泰");
		stations.add(station3006);
		
		Station station207 = new Station();
		station207.setId(207);
		station207.setName("飞翔公园");
		stations.add(station207);
		
		Station station208 = new Station();
		station208.setId(208);
		station208.setName("三元里");
		stations.add(station208);
		
		Station station209 = new Station();
		station209.setId(209);
		station209.setName("广州火车站");
		stations.add(station209);
		
		Station station210 = new Station();
		station210.setId(210);
		station210.setName("越秀公园");
		stations.add(station210);
		
		Station station211 = new Station();
		station211.setId(211);
		station211.setName("纪念堂");
		stations.add(station211);
		
		Station station212 = new Station();
		station212.setId(212);
		station212.setName("公园前");
		stations.add(station212);
		
		Station station213 = new Station();
		station213.setId(213);
		station213.setName("海珠广场");
		stations.add(station213);
		
		Station station214 = new Station();
		station214.setId(214);
		station214.setName("市二宫");
		stations.add(station214);
		
		Station station215 = new Station();
		station215.setId(215);
		station215.setName("江南西");
		stations.add(station215);
		
		Station station216 = new Station();
		station216.setId(216);
		station216.setName("昌岗");
		stations.add(station216); 
		
		Station station217 = new Station();
		station217.setId(217);
		station217.setName("江泰路");
		stations.add(station217);


		Station station218 = new Station();
		station218.setId(218);
		station218.setName("东晓南");
		stations.add(station218);
		
		Station station219 = new Station();
		station219.setId(219);
		station219.setName("南洲");
		stations.add(station219);
		
		Station station220 = new Station();
		station220.setId(220);
		station220.setName("洛溪");
		stations.add(station220);
		
		Station station221 = new Station();
		station221.setId(221);
		station221.setName("南浦");
		stations.add(station221);
		
		Station station222 = new Station();
		station222.setId(222);
		station222.setName("会江");
		stations.add(station222);
		
		Station station223 = new Station();
		station223.setId(223);
		station223.setName("石壁");
		stations.add(station223);
		
		Station station224 = new Station();
		station224.setId(224);
		station224.setName("广州南站");
		stations.add(station224);
		
		line30.setId(30);
		line30.setName("3号线北延线");
		line30.setStation(stations);
		return line30;
	}
}
