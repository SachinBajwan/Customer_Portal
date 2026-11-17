package terms.defaultregion.view.bean;

import oracle.adf.controller.ControllerContext;

public class DynamicRegionBean 
{
    public void initializer(){
          System.out.println("in the initializer of TaskFlow " + ControllerContext.getInstance().getCurrentViewPort().getTaskFlowContext().getTaskFlowId());
          
        }

      public void finalizer(){
         System.out.println("in the finalizer of TaskFlow " + ControllerContext.getInstance().getCurrentViewPort().getTaskFlowContext().getTaskFlowId());
            
      }
}
