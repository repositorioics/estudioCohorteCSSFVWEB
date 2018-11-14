package ni.com.sts.estudioCohorteCssfv.openClinica;

import java.util.Date;

public class EventScheduleParams {
	String label;
	String eventDefinitionOID;
	String location;
	String identifier;
	String siteidentifier;
	Date startDate;
	Date endDate;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getEventDefinitionOID() {
		return eventDefinitionOID;
	}
	public void setEventDefinitionOID(String eventDefinitionOID) {
		this.eventDefinitionOID = eventDefinitionOID;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getSiteidentifier() {
		return siteidentifier;
	}
	public void setSiteidentifier(String siteidentifier) {
		this.siteidentifier = siteidentifier;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
