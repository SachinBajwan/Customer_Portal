package resourceapp.resource.view;

import javax.faces.event.ActionEvent;

import resourceapp.resource.view.bundle.Resource;

public class Test {
    public Test() {
    }

    public void click(ActionEvent actionEvent) {
        // Add event code here...
        Resource obj=new Resource();
       obj.getResourceFilePath();
    }
}
