/*
 * Copyright 2012-2013 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package clojusc.aws.examples.swf.javaapp;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;
import clojusc.aws.examples.swf.javaapp.GreeterConstants;

public class GreeterWorker  {

   public static void main() throws Exception {
     ClientConfiguration config = new ClientConfiguration().withSocketTimeout(
        GreeterConstants.clientTimeout);

     AWSCredentials creds = new BasicAWSCredentials(
        System.getenv(GreeterConstants.accessKey),
        System.getenv(GreeterConstants.secretKey));

     AmazonSimpleWorkflow service = new AmazonSimpleWorkflowClient(creds, config);
     service.setEndpoint(GreeterConstants.endpoint);

     ActivityWorker aw = new ActivityWorker(
        service, GreeterConstants.domain, GreeterConstants.taskListToPoll);
     aw.addActivitiesImplementation(new GreeterActivitiesImpl());
     aw.start();

     WorkflowWorker wfw = new WorkflowWorker(
        service, GreeterConstants.domain, GreeterConstants.taskListToPoll);
     wfw.addWorkflowImplementationType(GreeterWorkflowImpl.class);
     wfw.start();
   }
}
