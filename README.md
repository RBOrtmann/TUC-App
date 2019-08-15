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

### List of Needed Improvements
* App currently sends as many packets as possible when in `ControllerActivity`. It should only send a few packets when there is an update to the data.
* App disconnects when the device goes to sleep or when opening the app switcher. It should stay connected unless the user exits the app or logs out.
* App will remain in `ControllerActivity` even when it disconnects for one of the above reasons. If it disconnects for more than a couple of seconds for any reason, it should take the user back to the selection screen.
* `ControllerActivity` and `ControllerThread` are decipherable but too messy in my opinion. They should be refactored and cleaned up where possible.
* The layout of `ControllerActivity` is decent but could be improved in several areas. The buttons and joystick should inflate for larger screen sizes.
* There is currently no way to activate the SOS lights from the app. This functionality should be added.
* The app does not currently receive status updates from the TUC. It should, and update the UI accordingly.
  * Related to this, toggling the hazards desynchronizes the light drawable from the actual state of the lights on the TUC. This should be fixed.
  * The "TUC Mode" setting is shown on the Settings screen, but it is not currently being sent over to the TUC. It should be synchronized with the TUC state.
  
I'm sure there are many other improvements that can and should be made; these are only the ones that immediately came to mind and must be addressed before the app is ready to ship.
