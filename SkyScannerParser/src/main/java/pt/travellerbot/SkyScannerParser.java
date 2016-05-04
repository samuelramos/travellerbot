package SkyScannerParser;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SkyScannerParser
{
    public static void main(String[] args) throws Exception
    {
        int i, iTravelDuration, iTotalPrice;
        
        SkyScannerParser oSSP = new SkyScannerParser();
        List<HashMap> oOutboundTrips = new ArrayList<>();
        List<HashMap> oInboundTrips = new ArrayList<>();
        
        Scanner oScan = new Scanner(System.in);
        Date oOutboundDate, oInboundDate;
        SimpleDateFormat oOutputDateFormat = new SimpleDateFormat ("dd-MM-yyyy");
        
        //System.out.println("Airline,Depart Date,Depart Time,Depart Airport,Arrive Date,Arrive Time, Arrive Airport,Travel Time,Stops,Price");  
        
       for(i = 1; i < 32; i++)
        {
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/lis_vie_"+(i<10?"0"+i:i)+"07.txt", oOutboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/lis_vie_"+(i<10?"0"+i:i)+"08.txt", oOutboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/opo_vie_"+(i<10?"0"+i:i)+"07.txt", oOutboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/opo_vie_"+(i<10?"0"+i:i)+"08.txt", oOutboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/lis_bud_"+(i<10?"0"+i:i)+"07.txt", oOutboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/lis_bud_"+(i<10?"0"+i:i)+"08.txt", oOutboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/opo_bud_"+(i<10?"0"+i:i)+"07.txt", oOutboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/opo_bud_"+(i<10?"0"+i:i)+"08.txt", oOutboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/vie_lis_"+(i<10?"0"+i:i)+"07.txt", oInboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/vie_lis_"+(i<10?"0"+i:i)+"08.txt", oInboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/vie_opo_"+(i<10?"0"+i:i)+"07.txt", oInboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/vie_opo_"+(i<10?"0"+i:i)+"08.txt", oInboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/bud_lis_"+(i<10?"0"+i:i)+"07.txt", oInboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/bud_lis_"+(i<10?"0"+i:i)+"08.txt", oInboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/bud_opo_"+(i<10?"0"+i:i)+"07.txt", oInboundTrips);
            oSSP.parseTrips("D:/NB19594/Desktop/impcap/bud_opo_"+(i<10?"0"+i:i)+"08.txt", oInboundTrips);
        }

        while(true)
        {
            System.out.print("Dias de viagem: ");
            iTravelDuration = oScan.nextInt();
            if(iTravelDuration == 0)
                return;
            
            System.out.println("Airline (O),Depart Date (O),Depart Time (O),Depart Airport (O),Arrive Date (O),Arrive Time (O),Arrive Airport (O),Travel Time (O),Stops (O),Price (O),Airline (I),Depart Date (I),Depart Time (I),Depart Airport (I),Arrive Date (I),Arrive Time (I),Arrive Airport (I),Travel Time (I),Stops (I),Price (I),Total Price");
            
            for(HashMap oOutboundTrip : oOutboundTrips)
            {
                oOutboundDate = (Date)oOutboundTrip.get("DepartDate");
                for(HashMap oInboundTrip : oInboundTrips)
                {
                    oInboundDate = (Date)oInboundTrip.get("DepartDate");
                    if((oInboundDate.getTime()-oOutboundDate.getTime())/(1000*60*60*24) == iTravelDuration)
                    {
                        iTotalPrice = (int)oOutboundTrip.get("Price") + (int)oInboundTrip.get("Price");
                        System.out.println(oOutboundTrip.get("Airline") + "," + 
                               oOutputDateFormat.format(oOutboundDate) + "," + 
                               oOutboundTrip.get("DepartTime") + "," + 
                               oOutboundTrip.get("DepartAirport") + "," + 
                               oOutputDateFormat.format((Date)oOutboundTrip.get("ArriveDate")) + "," + 
                               oOutboundTrip.get("ArriveTime") + "," + 
                               oOutboundTrip.get("ArriveAirport") + "," + 
                               oOutboundTrip.get("TravelTime") + "," + 
                               oOutboundTrip.get("Stops") + "," + 
                               oOutboundTrip.get("Price") + "," + 
                               oInboundTrip.get("Airline") + "," + 
                               oOutputDateFormat.format(oInboundDate) + "," + 
                               oInboundTrip.get("DepartTime") + "," + 
                               oInboundTrip.get("DepartAirport") + "," + 
                               oOutputDateFormat.format((Date)oInboundTrip.get("ArriveDate")) + "," + 
                               oInboundTrip.get("ArriveTime") + "," + 
                               oInboundTrip.get("ArriveAirport") + "," + 
                               oInboundTrip.get("TravelTime") + "," + 
                               oInboundTrip.get("Stops") + "," + 
                               oInboundTrip.get("Price") + "," + 
                               iTotalPrice);
                    }
                }
            }
        }
    }
    
    private void parseTrips(String pathname, List oTrips) throws Exception
    {
        String sDepartDate, sArriveTime, sTravelTime, sPrice;
        HashMap oTrip;
        
        Date oDepartDate;
        Calendar oArriveCalendar = Calendar.getInstance();
        SimpleDateFormat oInputDateFormat = new SimpleDateFormat ("dd/MM/yy");
        //SimpleDateFormat oOutputDateFormat = new SimpleDateFormat ("dd-MM-yyyy");
        
        File input = new File(pathname);
        Document oHTMLDoc = Jsoup.parse(input, "UTF-8");
        Elements oHTMLTrips = oHTMLDoc.select("li.day-list-item");
        
        sDepartDate = oHTMLDoc.select("div.search-summary-date-nudger span.date").first().text();
        oDepartDate = oInputDateFormat.parse(sDepartDate);
        
        for (Element oHTMLTrip : oHTMLTrips) {
            
            sArriveTime = oHTMLTrip.select("div.arrive span").first().text();
            oArriveCalendar.setTime(oDepartDate);
            if(sArriveTime.length() > 5)
            {
                oArriveCalendar.add(Calendar.DATE, Integer.valueOf(""+sArriveTime.charAt(8)));
                sArriveTime = sArriveTime.substring(0,5);
            }
            
            sTravelTime = oHTMLTrip.select("div.stops span").first().text();
            if(sTravelTime.indexOf("h") + 1 == sTravelTime.length())
                sTravelTime += " 00m";
            if(sTravelTime.length() < 7)
                sTravelTime = "0" + sTravelTime;
            
            sPrice = oHTMLTrip.select("div.mainquote-minimal-price").first().text();
            
            oTrip = new HashMap();
            oTrip.put("Airline", oHTMLTrip.select("div.airline span").first().text());
            oTrip.put("DepartDate", oDepartDate);
            oTrip.put("DepartTime", oHTMLTrip.select("div.depart span").first().text());
            oTrip.put("DepartAirport", oHTMLTrip.select("div.depart span").last().text());
            oTrip.put("ArriveDate", oArriveCalendar.getTime());
            oTrip.put("ArriveTime", sArriveTime);
            oTrip.put("ArriveAirport", oHTMLTrip.select("div.arrive span").last().text());
            oTrip.put("TravelTime", sTravelTime);
            oTrip.put("Stops", oHTMLTrip.select("div.leg-stops span").first().text());
            oTrip.put("Price", Integer.valueOf(sPrice.substring(6, sPrice.length()-2)));
            oTrips.add(oTrip);
            
            /*System.out.println(oTrip.get("Airline") + "," + 
                               oOutputDateFormat.format((Date)oTrip.get("DepartDate")) + "," + 
                               oTrip.get("DepartTime") + "," + 
                               oTrip.get("DepartAirport") + "," + 
                               oOutputDateFormat.format((Date)oTrip.get("ArriveDate")) + "," + 
                               oTrip.get("ArriveTime") + "," + 
                               oTrip.get("ArriveAirport") + "," + 
                               oTrip.get("TravelTime") + "," + 
                               oTrip.get("Stops") + "," + 
                               oTrip.get("Price"));*/
        }
    }
}
