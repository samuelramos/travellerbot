package pt.travellerbot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SkyScannerParser
{
    public static void main(String[] args) throws Exception
    {
        SkyScannerParser http = new SkyScannerParser();
        System.out.println("Airline,Depart Date,Depart Time,Depart Airport,Arrive Date,Arrive Time, Arrive Airport,Travel Time,Stops,Price");  
        for(int i = 1; i < 32; i++)
            http.jsoupTest("impcap/vie_lis_"+(i<10?"0"+i:i)+"07.txt");
        for(int i = 1; i < 32; i++)
            http.jsoupTest("impcap/vie_lis_"+(i<10?"0"+i:i)+"08.txt");
        for(int i = 1; i < 32; i++)
            http.jsoupTest("impcap/vie_opo_"+(i<10?"0"+i:i)+"07.txt");
        for(int i = 1; i < 32; i++)
            http.jsoupTest("impcap/vie_opo_"+(i<10?"0"+i:i)+"08.txt");
        for(int i = 1; i < 32; i++)
            http.jsoupTest("impcap/bud_lis_"+(i<10?"0"+i:i)+"07.txt");
        for(int i = 1; i < 32; i++)
            http.jsoupTest("impcap/bud_lis_"+(i<10?"0"+i:i)+"08.txt");
        for(int i = 1; i < 32; i++)
            http.jsoupTest("impcap/bud_opo_"+(i<10?"0"+i:i)+"07.txt");
        for(int i = 1; i < 32; i++)
            http.jsoupTest("impcap/bud_opo_"+(i<10?"0"+i:i)+"08.txt");
    }
    
    private void jsoupTest(String pathname) throws Exception
    {
        String sAirline;
        String sDepartDate;
        String sDepartTime;
        String sDepartAirport;
        String sArriveTime;
        String sArriveAirport;
        String sTravelTime;
        String sStops;
        String sPrice;
        
        Date oDepartDate;
        SimpleDateFormat oInputDateFormat = new SimpleDateFormat ("dd/MM/yy");
        SimpleDateFormat oOutputDateFormat = new SimpleDateFormat ("dd-MM-yyyy");
        Calendar oArriveDate = Calendar.getInstance();
        
        File input = new File(pathname);
        Document doc = Jsoup.parse(input, "UTF-8");
        Elements trips = doc.select("li.day-list-item");
        
        sDepartDate = doc.select("div.search-summary-date-nudger span.date").first().text();
        oDepartDate = oInputDateFormat.parse(sDepartDate);
        
        for (Element trip : trips) {
            
            sAirline = trip.select("div.airline span").first().text();
            sDepartTime = trip.select("div.depart span").first().text();
            sDepartAirport = trip.select("div.depart span").last().text();
            sArriveTime = trip.select("div.arrive span").first().text();
            sArriveAirport = trip.select("div.arrive span").last().text();
            sTravelTime = trip.select("div.stops span").first().text();
            if(sTravelTime.indexOf("h") + 1 == sTravelTime.length()) sTravelTime += " 00m";
            if(sTravelTime.length() < 7) sTravelTime = "0" + sTravelTime;
            sStops = trip.select("div.leg-stops span").first().text();
            sPrice = trip.select("div.mainquote-minimal-price").first().text();
            sPrice = sPrice.substring(6, sPrice.length()-2);
            
            oArriveDate.setTime(oDepartDate);
            if(sArriveTime.length() > 5)
            {
                oArriveDate.add(Calendar.DATE, Integer.valueOf(""+sArriveTime.charAt(8)));
                sArriveTime = sArriveTime.substring(0,5);
            }
            
            System.out.println(sAirline + "," + 
                               oOutputDateFormat.format(oDepartDate) + "," + 
                               sDepartTime + "," + 
                               sDepartAirport + "," + 
                               oOutputDateFormat.format(oArriveDate.getTime()) + "," + 
                               sArriveTime + "," + 
                               sArriveAirport + "," + 
                               sTravelTime + "," + 
                               sStops + "," + 
                               sPrice);            
        }
    }
}
