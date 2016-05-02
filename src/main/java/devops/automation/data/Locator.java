package devops.automation.data;

public class Locator {
	private String name = null;
	private String type = null;
	private String locatorType = null;
	private String locator = null;
	private String page = null;
	
	public Locator(String name, String type, String locatorType, String locator, String page) {
		super();
		this.name = name;
		this.type = type;
		this.locatorType = locatorType;
		this.locator = locator;
		this.page = page;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getLocatorType() {
		return locatorType;
	}
	
	public void setLocatorType(String locatorType) {
		this.locatorType = locatorType;
	}
	
	public String getLocator() {
		return locator;
	}
	
	public void setLocator(String locator) {
		this.locator = locator;
	}
}
