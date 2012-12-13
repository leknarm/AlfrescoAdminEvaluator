package th.co.osdev.alfresco.evaluator;

import org.alfresco.web.evaluator.BaseEvaluator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.springframework.extensions.surf.WebFrameworkServiceRegistry;
import org.springframework.extensions.webscripts.ScriptRemote;
import org.springframework.extensions.webscripts.ScriptRemoteConnector;
import org.springframework.extensions.webscripts.connector.Response;

public class AdminEvaluator extends BaseEvaluator {
    
    private WebFrameworkServiceRegistry serviceRegistry;
    
    public void setServiceRegistry(WebFrameworkServiceRegistry serviceRegistry)
    {
        this.serviceRegistry = serviceRegistry;
    }
    
	@Override
    public boolean evaluate(JSONObject jsonObject)
    {
    	ScriptRemote scriptRemote = serviceRegistry.getScriptRemote();
    	ScriptRemoteConnector connector = scriptRemote.connect("alfresco");
        Response response = connector.get("/api/groups/ALFRESCO_ADMINISTRATORS/children");
        if (response.getStatus().getCode() == 200)
        {
			try 
			{
				org.json.JSONObject adminUsers = new org.json.JSONObject(response.getResponse());
	            JSONArray admins = adminUsers.getJSONArray("data");
	            for (int i = 0 ; i < admins.length() ; i++ )
	            {
	            	String shortName = admins.getJSONObject(i).getString("shortName");
	            	if (shortName.trim().equalsIgnoreCase(getUserId()))
	            	{
	            		return true;
	            	}
	            }
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
        }
        return false;
    }
}
