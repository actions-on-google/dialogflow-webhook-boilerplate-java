# Actions on Google: Java client library boilerplate

Boilerplate to help you get started quickly with the Java client library for Actions on Google.

## Setup Instructions

### Webhook
The boilerplate includes entry points for both Java servlets and AWS Lambda.
* If using Servlets,
    1. Delete ActionsAWSHandler.java
    1. Remove dependencies in build.gradle for AWS.
    1. Build your project's jar file using gradle.
    1. Deploy your servlet following instructions on [Google Cloud Platform App Engine](https://cloud.google.com/appengine/)
* If using AWS lambda,
    1. Delete ActionsServlet
    1. Remove corresponding dependencies for Servlets in build.gradle.
    1. Build the AWS compatible zip file using the buildAWSZip gradle task.
    1. Deploy the zip in AWS by following instructions at https://aws.amazon.com/lambda/

### Action configuration
1. Use the [Actions on Google Console](https://console.actions.google.com) to add a new project with a name of your choosing and click *Create Project*.
1. Click *Skip*, located on the top right.
1. On the left navigation menu under *BUILD*, click on *Actions*. Click on *Add Your First Action* and choose your app's language(s).
1. Select *Custom intent*, click *BUILD*. This will open a Dialogflow console. Click *CREATE*.
1. Click on the gear icon to see the project settings.
1. Add new intents for your Action.
1. Deploy the fulfillment webhook as described in the previous section
1. Go back to the Dialogflow console and select *Fulfillment* from the left navigation menu. Enable *Webhook*, set the value of *URL* to the webhook from the previous section, then click *Save*.


## Test on the Actions on Google simulator
1. Select [*Integrations*](https://console.dialogflow.com/api-client/#/agent//integrations) from the left navigation menu and open the *Settings* menu for Actions on Google.
1. Enable *Auto-preview changes* and Click *Test*. This will open the Actions on Google simulator.
1. Type `Talk to my test app` in the simulator, or say `OK Google, talk to my test app` to any Actions on Google enabled device signed into your developer account.

For more detailed information on deployment, see the [documentation](https://developers.google.com/actions/dialogflow/deploy-fulfillment).

## References and How to report bugs
* Actions on Google documentation: [https://developers.google.com/actions/](https://developers.google.com/actions/).
* If you find any issues, please open a bug here on GitHub.
* Questions are answered on [StackOverflow](https://stackoverflow.com/questions/tagged/actions-on-google).

## How to make contributions?
Please read and follow the steps in the [CONTRIBUTING.md](CONTRIBUTING.md).

## License
See [LICENSE](LICENSE).

## Terms
Your use of this sample is subject to, and by using or downloading the sample files you agree to comply with, the [Google APIs Terms of Service](https://developers.google.com/terms/).

## Google+
Actions on Google Developers Community on Google+ [https://g.co/actionsdev](https://g.co/actionsdev).