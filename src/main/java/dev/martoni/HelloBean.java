package dev.martoni;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.Date;

@ManagedBean
@RequestScoped
public class HelloBean implements Serializable {

    public String getCurrentTime() {
        return new Date().toString();
    }
}
