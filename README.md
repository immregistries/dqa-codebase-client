The general philosophy of the Codebase project is to provide a service to the IIS community that will maintain a common set of codes that are relevant to the IIS community. 

This will include review the CDC codesets and will add relevant information.  
  
There is a new CDC project to deliver vetted Vaccine code sets to the IIS community. We expect that the new project will meet our needs, but I don't want to pin a project to the expectation that this project will get it right the first time and that they will cover all the concepts we need.

Also, we expect we are going to lead the CDC project in finding new coded concepts that need to represented and we don't want to wait for CDC to agree before we can move forward on our end.

Beyond this there are more practical benefits: 

We don't want to hook our project directly to the CDC project, if they make a mistake we have no buffer.  

We need to have a manual check and sign off from the community before any concepts change in the MQE.  

The same with an IIS, sure it's important to have tools to automate this process so we really need the CDC to have a web service
  
But we should have another step where we verify the concepts and make sure we have the setup correctly before we let production systems depend on them.

I've had a lot of experience with CDC not getting the needs at the production level and making changes that break our model.

So a buffer is critical. That is what the MQE code base project is for us.