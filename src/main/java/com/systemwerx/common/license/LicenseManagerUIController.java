package com.systemwerx.common.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

import com.systemwerx.common.event.Log;
import com.systemwerx.common.util.FXOptionPane;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 */
public class LicenseManagerUIController {
	static LicenseManagerUI thelicenseManager;
	String productID = "01";
	String licensePath = "license.dat";
	String productName = "Mimic Workbench";
	LicenseManagerUI licenseManagerUI;

	LicenseManagerNotification notify;
	Stage callerStage;
	boolean traceActive = false;
	boolean terminateOnLicenseApply = false;
	boolean licenseValid = false;

	@FXML
	Button okButton;

	@FXML
	Button cancelButton;

	@FXML
	TextArea taArea0;

	@FXML
	TextField taArea1;

	@FXML
	public void initialize() {
	}

	public LicenseManagerUIController() {
	}

	@FXML
	public void init(LicenseManagerUI licenseManagerUI, String prodID, String iProductName, String iLicensePath,
			LicenseManagerNotification callback,
			boolean terminateOnApply) {
		notify = callback;
		terminateOnLicenseApply = terminateOnApply;
		productID = prodID;
		this.licenseManagerUI = licenseManagerUI;
		if (iLicensePath != null)
			licensePath = iLicensePath;
		if (iProductName != null)
			productName = iProductName;
		if (System.getProperty("sxdebug") != null) {
			traceActive = true;
		}
		loadLicense();
		taArea1.requestFocus();
	}

	@FXML
	protected void handleOkButton(ActionEvent event) {
		Log.debug("Ok button selected");
		String data = taArea1.getText();
		if (storeLicense(taArea1.getText())) {
			if (terminateOnLicenseApply) {
				if ( notify != null ) {
						notify.notifyLicenseOk();
				}
			}
		}
		taArea1.setText("");
	}

	@FXML
	protected void handleCancelButton(ActionEvent event) {
		Log.debug("Cancel button selected");
		licenseManagerUI.getStage().hide();
	}

	boolean storeLicense(String Key) {
		try {
			String licenseFile;
			if ((licenseFile = System.getProperty("sxlicense")) == null) {
				licenseFile = licensePath;
			} else {
				if (traceActive)
					Log.trace("License file selected : " + licenseFile);
			}
			license lic = new license(productID);
			File out = new File(licenseFile);

			if (Key.length() != 49) {
				displayErrorMessage("License is invalid - length is incorrect");
				return false;
			}

			if (lic.verifyLicense(Key.substring(0, 5), Key.substring(5, 9), Key.substring(9))) {
				displayInfoMessage("License is valid - expires "
						+ lic.getExpireDateString(Key.substring(0, 5)));

				FileOutputStream fos = new FileOutputStream(licenseFile);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeUTF(Key);
				oos.close();
				licenseValid = true;
				loadLicense();
				taArea1.requestFocus();
				return true;
			} else {
				displayErrorMessage("License is not valid ");
				return false;
			}
		} catch (Exception ex) {
			Log.error("Exception detected" + ex.getMessage());
		}
		LicenseManager.getLicenseManagerStatic().notify();
		return true;
	}

	void displayInfoMessage(String message) {
		Log.info(message);
		FXOptionPane.showMessageDialog(licenseManagerUI.getStage(), message, "Information");
	}

	void displayErrorMessage(String message) {
		Log.error(message);
		FXOptionPane.showErrorDialog(licenseManagerUI.getStage(), message, "Error");
	}

	boolean loadLicense() {
		String licenseFile;
		Calendar expireDate;
		try {
			if ((licenseFile = System.getProperty("e3license")) == null) {
				if ((licenseFile = System.getProperty("sxlicense")) == null) {
					licenseFile = licensePath;
				}
			} else {
				if (traceActive)
					Log.trace("License file selected : " + licenseFile);
			}
			license lic = new license(productID);
			File in = new File(licenseFile);
			FileInputStream fis = new FileInputStream(licenseFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			String Key = null;
			Key = ois.readUTF();
			if (traceActive)
				Log.trace("License : " + Key);

			if (lic.verifyLicense(Key.substring(0, 5), Key.substring(5, 9), Key.substring(9))) {
				taArea0.setText(productName + " - expires " + lic.getExpireDateString(Key.substring(0, 5)));
				return true;
			} else {
				expireDate = lic.getExpireCalendar(Key.substring(0, 5));
				if (lic.isAfterExpiry(expireDate)) {
					taArea0.setText(productName + " - expired " + lic.getExpireDateString(Key.substring(0, 5)));
				} else {
					taArea0.setText(productName + " - expires " + lic.getExpireDateString(Key.substring(0, 5)) +
							"\n license is invalid \n( Key may not be valid for this system)");
				}
				return false;
			}
		} catch (java.io.FileNotFoundException ex) {
			taArea0.setText("License not found");
			return false;
		}

		catch (java.io.EOFException ex) {
			return false;
		} catch (Exception ex) {
			Log.error("Exception detected" + ex.getMessage());
			return false;
		}
	}

	public boolean isTerminateOnLicenseApply() {
		return terminateOnLicenseApply;
	}

	public void setTerminateOnLicenseApply(boolean terminateOnLicenseApply) {
		this.terminateOnLicenseApply = terminateOnLicenseApply;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

}
