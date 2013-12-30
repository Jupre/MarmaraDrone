
package com.f1bilisim.ardrone.navdata.javadrone;

import java.nio.ByteBuffer;

import com.f1bilisim.ardrone.navdata.NavDataException;


public class JavadroneNavDataParser {
	private NavDataListener navDataListener;
	
	long lastSequenceNumber=1;
	
	//dinleyici ayarlarý
	public void setNavDataListener(NavDataListener navDataListener){
		this.navDataListener=navDataListener;
	}
		
	public void parseNavData(ByteBuffer buffer) throws NavDataException{
		
		dispatch(buffer, buffer.remaining());
	}
	
	private void dispatch(ByteBuffer optionData, int length)
	{
		try
		{
			NavData navData = NavData.createFromData(optionData, length);
			
			if(navDataListener!=null){
				navDataListener.navDataUpdated(navData);
			}

		}
		catch (NavDataFormatException e)
		{
			e.printStackTrace();
		}
	}
}