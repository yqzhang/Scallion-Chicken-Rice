package bit.scallionchickenrice.leo.tool;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ActiveInfoContentProvider implements IStructuredContentProvider{
	public Object[] getElements(Object element){
        if(element instanceof List)
            return ((List<?>)element).toArray();
        else
            return new Object[0];
	}

	public void dispose() {}

	public void inputChanged(Viewer v, Object oldInput, Object newInput){}

}
