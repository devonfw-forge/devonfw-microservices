package com.devonfw.demoquarkus;

public class ApplicationAccessControlConfig {
	public static final String APP_ID = "demo-quarkus";
	
	private static final String PREFIX = APP_ID + ".";
	
	public static final String PERMISSION_FIND_OBJECT = PREFIX + "FindObject";
	
	public static final String PERMISSION_SAVE_OBJECT = PREFIX + "SaveObject";
	
	public static final String PERMISSION_DELETE_OBJECT = PREFIX + "DeleteObject";
	
	public ApplicationAccessControlConfig() {
		
	}
}
