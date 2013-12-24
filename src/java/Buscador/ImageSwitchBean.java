/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;

/**
 *
 * @author mateo
 */
@ManagedBean
@ApplicationScoped
public class ImageSwitchBean {  
  
    private List<String> images;  
  
    public ImageSwitchBean() {  
        images = new ArrayList<String>();  
        images.add("titulo1.jpg");  
        images.add("titulo2.jpg");  
        images.add("titulo3.jpg");  
        images.add("titulo4.jpg");  
        images.add("titulo5.jpg");  
    }  
  
    public List<String> getImages() {  
        return images;  
    }  
} 
