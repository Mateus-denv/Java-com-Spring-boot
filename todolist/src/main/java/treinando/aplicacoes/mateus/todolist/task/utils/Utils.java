package treinando.aplicacoes.mateus.todolist.task.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


public class Utils {
    // Pasar o que está dentro do source para o target
    public static void copyNonNullProperties(Object source, Object target){
        BeanUtils.copyProperties(source, target, getNonNullPropertiesNomes(source));
    }
    // Convertendo para as propiedade que são nulas
    public static String[] getNonNullPropertiesNomes(Object source){
        final BeanWrapper src = new BeanWrapperImpl(source);

        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();

        for (PropertyDescriptor pd : pds){
            Object srcVaue = src.getPropertyValue(pd.getName());
            if (srcVaue == null){
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result); 
    }
}
