package com.ustclin.petchicken.listview;

public interface OnRefreshListener {

	/**
	 * 下拉刷新执行的刷新任务, 使用时, 
	 * 当刷新完毕之后, 需要手动的调用onRefreshFinish(), 去隐藏头布局
	 */
	public void onRefresh();
	
}
