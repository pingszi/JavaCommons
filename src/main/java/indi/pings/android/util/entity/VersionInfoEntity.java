package indi.pings.android.util.entity;

import indi.pings.android.util.entity.base.BaseEntity;

/**
 *********************************************************
 ** @desc  ：版本信息                                             
 ** @author  Pings                                      
 ** @date    2016年5月20日  
 ** @version v1.0                                                                                  
 * *******************************************************
 */
public class VersionInfoEntity extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	//**安装文件名称
	private String name;
	//**版本号
	private Integer versionCode;
	//**版本名称
	private String versionName;  
	//**下载地址
	private String url;  
	//**更新信息
    private String updateInfo;
	//**版本描述
	private String desc;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUpdateInfo() {
		return updateInfo;
	}
	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		else if(!(o instanceof VersionInfoEntity))
			return false;	
		
		VersionInfoEntity ver = (VersionInfoEntity)o;
		if(ver.getVersionCode() != null)
			return ver.getVersionCode() == this.getVersionCode();	
		else if(ver.getVersionName() != null)
			return ver.getVersionName().equals(this.getVersionName());
		
		throw new RuntimeException("newVer is null");
	}

}
