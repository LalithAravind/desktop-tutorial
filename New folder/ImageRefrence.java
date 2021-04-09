package com.cognizant.cloudone.cl.azure.entity.v2;

/**
 * 
 */

/**
 * @author 411967
 *
 */
public class ImageRefrence {
	
	 private String id;

	    private String location;

	    private String name;

	    public String getId ()
	    {
	        return id;
	    }

	    public void setId (String id)
	    {
	        this.id = id;
	    }

	    public String getLocation ()
	    {
	        return location;
	    }

	    public void setLocation (String location)
	    {
	        this.location = location;
	    }

	    public String getName ()
	    {
	        return name;
	    }

	    public void setName (String name)
	    {
	        this.name = name;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [id = "+id+", location = "+location+", name = "+name+"]";
	    }

}
