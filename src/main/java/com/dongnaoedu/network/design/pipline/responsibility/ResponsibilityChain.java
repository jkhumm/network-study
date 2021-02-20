package com.dongnaoedu.network.design.pipline.responsibility;

import java.util.ArrayList;
import java.util.List;

public class ResponsibilityChain {

	private List<Responsibility> responsibilitys;

	private int index = 0;

	public ResponsibilityChain() {
		this.responsibilitys = new ArrayList<>();
	}

	public void process(Request request) {
		if (this.index < this.responsibilitys.size()) {
			this.responsibilitys.get(index++).process(request, this);
		}
	}

	public void register(Responsibility res) {
		this.responsibilitys.add(res);
	}
}
