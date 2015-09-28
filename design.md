## Design
1. TimeItem
	1. 用于管理事件项，比如坐地铁，吃饭
	2. 文件：mt-items.txt
	3. 格式:  	坐地铁;吃饭

2. RecordItem
	1. 用于记录详情，即：某天某事情花了多长时间，等级
	2. 文件：mt-recordTimes.txt 
	3. 格式	 2015-09-23:坐地铁:0.5:2;
	
3. Todos task记录
	1. 用于记录 当天，当周，当月的tasks
	2. 文件: mt-tasks-daily.txt
	   格式: 2015-3-9:拿相片;
		
		文件: mt-tasks-weekly.txt
		格式: 2015-23:相册; ## 23为 week of year
		
		文件: mt-tasks-monthly.txt 
		格式: 2015-9:相册;

	
	
### sdf
	1. activity_list_item
	2. simple_spinner_dropdown_item
	
	```
	public static final int simple_spinner_dropdown_item=0x01090009;
    public static final int activity_list_item=0x01090000;
    
	```