package es.zaldo.petstore.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class that represents an error that was thrown.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Error {

    private int status;
    private String message;

    /**
     * Constructor of the class used by JAXB.
     */
    @SuppressWarnings("unused")
    private Error() {}

    /**
     * Constructor of the class.
     *
     * @param status Status code of the error
     * @param message Description of the error
     */
    public Error(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Error [status=" + status + ", message=" + message + "]";
    }

}
