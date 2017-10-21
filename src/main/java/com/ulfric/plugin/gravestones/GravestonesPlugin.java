package com.ulfric.plugin.gravestones;

import com.ulfric.plugin.Plugin;

public class GravestonesPlugin extends Plugin {

	public GravestonesPlugin() {
		install(GravestonesListener.class);
	}

}
