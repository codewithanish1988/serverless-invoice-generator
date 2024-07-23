# Details

This project is used in AWS lambda function as a serverless application, which can generate an Invoice PDF based on the request received in an SQS queue.  
The generated pdf is then uploaded to S3 and the corresponding S3 url is shared as an email message to the  email id received in the SQS request. 

# Points to Note
1. #### From email address
   Change the value of **from.email.address** in the file *src/main/resources/config.properties*, with the valid email id verified in SES.

2. #### S3 bucket name
    Change the value of **s3.bucket.name** in the file *src/main/resources/config.properties*, with the valid S3 bucket name.

3. #### S3 key prefix 
   Change the value of **s3.key.prefix** in the file *src/main/resources/config.properties*, with a valid directory name (ending with /). This directory will be crated in the S3 bucket and inside which PDF will be stored.

# Build and Deployment

1. #### To build the jar file 
   `mvn clean package shade:shade`

2. #### To create the lambda function with the jar created above

    `aws lambda create-function --function-name <lambda function name> \
   --runtime <Java run time> \
   --handler <Java class and method name of the RequestHandler> \
   --timeout <timeout value in seconds. Default 3 second>
   --role <arn of the role created for lambda> \
   --zip-file fileb://<path to jar file>`

3. #### To upload the jar file to lambda function after any code change
    `aws lambda update-function-code --function-name  <lambda function name> --zip-file fileb://<path to latest jar file>`

4. #### To link the SQS queue to the lambda function
    `aws lambda create-event-source-mapping --function-name <lambda function name> --event-source-arn <arn of the SQS queue>`

# Video Reference
For detailed video explanation of this project please visit my YouTube channel [CodeWithAnish](https://youtu.be/9fJWVlO2ZSw)
