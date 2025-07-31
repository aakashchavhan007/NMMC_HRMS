package com.nmmc.hrms.Utils;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class IpGenerator {

    private static final String PUBLIC_IP_API = "https://api.ipify.org?format=json";
    private final String API_URL = "http://ipinfo.io/";

    private final String[] HEADERS_LIST = { 
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR" 
    };

    // public String[] getIpAddressesFromRequest(HttpServletRequest request) {
    //     String ipv4 = null;
    //     String ipv6 = null;

    //     for (String header : HEADERS_LIST) {
    //         String ip = request.getHeader(header);
    //         if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
    //             if (ip.contains(".")) {
    //                 ipv4 = ip;
    //             } else if (ip.contains(":")) {
    //                 ipv6 = ip;
    //             }
    //         }
    //     }

    //     if (ipv4 == null) {
    //         ipv4 = request.getRemoteAddr();
    //     }
    //     if (ipv6 == null) {
    //         ipv6 = request.getRemoteAddr();
    //     }

    //     return new String[]{ipv4, ipv6};
    // }

    public String getIpAddressFromRequest(HttpServletRequest request) {
		for (String header : HEADERS_LIST) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}
		return request.getRemoteAddr(); 
	}

     // Method to get the public IP address
     public String getPublicIp() {
        String publicIp = null;
        try {
            URL url = new URL(PUBLIC_IP_API);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            publicIp = jsonResponse.getString("ip");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicIp;
    }

    // Method to get ISP information by IP
    // public String getIspByIp(String ip) {
    //     String isp = null;
    //     try {
    //         URL url = new URL(API_URL + ip + "/json");
    //         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    //         conn.setRequestMethod("GET");

    //         BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    //         String inputLine;
    //         StringBuilder response = new StringBuilder();

    //         while ((inputLine = in.readLine()) != null) {
    //             response.append(inputLine);
    //         }
    //         in.close();

    //         JSONObject jsonResponse = new JSONObject(response.toString());
    //         System.out.println("JSON Response: " + jsonResponse.toString(2)); // Pretty print the JSON response

    //         if (jsonResponse.has("org")) {
    //             isp = jsonResponse.getString("org");
    //         } else {
    //             isp = "ISP information not available";
    //         }

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         isp = "Error retrieving ISP information";
    //     }
    //     return isp;
    // }

    public String getMacId() {
        try {
            // Get all network interfaces
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();

                // Get the MAC address
                byte[] mac = network.getHardwareAddress();
                if (mac != null) {
                    System.out.print("Interface: " + network.getName() + " - MAC address: ");

                    // Convert the MAC address bytes to a human-readable format
                    StringBuilder macAddress = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        macAddress.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    System.out.println(macAddress.toString());
                    return macAddress.toString();
                }
            }
            return "MAC address not found";
            
        } catch (SocketException e) {
            System.err.println("Error getting network interfaces: " + e.getMessage());
            return "Error getting network interfaces: " + e.getMessage();
        }
    }
    
    
    public String GetAddress() {
        String address = "";
        InetAddress lanIp = null;
        try {

            String ipAddress = null;
            Enumeration<NetworkInterface> net = null;
            net = NetworkInterface.getNetworkInterfaces();

            while (net.hasMoreElements()) {
                NetworkInterface element = net.nextElement();
                Enumeration<InetAddress> addresses = element.getInetAddresses();

                while (addresses.hasMoreElements() && element.getHardwareAddress()!=null && element.getHardwareAddress().length > 0 && !isVMMac(element.getHardwareAddress())) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address) {

                        if (ip.isSiteLocalAddress()) {
                            ipAddress = ip.getHostAddress();
                            lanIp = InetAddress.getByName(ipAddress);
                        }

                    }

                }
            }

            if (lanIp == null)
                return null;

                address = getMacAddress(lanIp);

        } catch (UnknownHostException ex) {

            ex.printStackTrace();

        } catch (SocketException ex) {

            ex.printStackTrace();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return address;

    }

    private static String getMacAddress(InetAddress ip) {
        String address = null;
        try {

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            address = sb.toString();

        } catch (SocketException ex) {

            ex.printStackTrace();

        }

        return address;
    }

    private static boolean isVMMac(byte[] mac) {
        if(null == mac) return false;
        byte invalidMacs[][] = {
                {0x00, 0x05, 0x69},             //VMWare
                {0x00, 0x1C, 0x14},             //VMWare
                {0x00, 0x0C, 0x29},             //VMWare
                {0x00, 0x50, 0x56},             //VMWare
                {0x08, 0x00, 0x27},             //Virtualbox
                {0x0A, 0x00, 0x27},             //Virtualbox
                {0x00, 0x03, (byte)0xFF},       //Virtual-PC
                {0x00, 0x15, 0x5D}              //Hyper-V
        };

        for (byte[] invalid: invalidMacs){
            if (invalid[0] == mac[0] && invalid[1] == mac[1] && invalid[2] == mac[2]) return true;
        }

        return false;
    }
}
