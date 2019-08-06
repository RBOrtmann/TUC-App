<p><a href="https://www.ring-co.com/tuctrack/"><img src="https://github.com/S1lentHurr1cane/TUC-App/blob/master/resources/ring-co_TUC-01.svg" alt="" /></a></p>

# TUC Companion App
This repository contains the source code for the official TUC Companion App. More information on Ring-Co can be found [here](https://www.ring-co.com/). More information on the TUC can be found [here](https://www.ring-co.com/tuctrack/).

The TUC Companion App allows for remote control of the TUC's movement and attachments and limited configuration of the TUC's settings. This functionality is useful for those wanting to perform a task without being physically seated on the TUC themselves or for those who want to control the TUC while someone else is sitting on it.

## Example

PUT DEMO VIDEO HERE

## Installation

The official .apk can be downloaded here [PUT DOWNLOAD LINK HERE] and installed on devices running Android 6.0 Marshmallow or newer. Note that the app requires the `ACCESS_COARSE_LOCATION` permission in order to view the SSID of the currently connected WiFi network. Allowing the app this permission is _mandatory_, otherwise it will not be able to determine that the host device is connected to the TUC's WiFi network.

## Usage

Once installed, the app can be found in the app drawer. Launching the app displays a login page. The default password can be found in the TUC's manual, but the password can be changed after the initial login. Upon login, the user is taken to a selection screen. Selecting the gear icon will bring the user to a settings screen. If the user selects the controller icon, however, the app will check if the device is connected to a TUC network. If the device is not connected to a TUC network, a message is displayed asking the user to connect to a TUC network. If the device _is_ connected to a TUC network, the user is taken to a controller screen, from which the user can begin remotely controlling the TUC. From this page, the user can navigate to the settings screen or logout by clicking the context menu icon in the upper right hand corner.

## Notes

Although I am reasonably familiar with Java, I am, admittedly, a novice Android developer. As such, this app by no means approaches perfection and could be improved upon in many areas with respect to both visual style and programming practice. To be clear, this was a first attempt at understanding the Android API and I merely cobbled together an app with the necessary functionality in just a couple months. Constructive criticism and feedback are always welcome and should be given in as much detail as possible under the 'Issues' tab of this repository.
