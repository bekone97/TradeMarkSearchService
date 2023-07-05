package by.intexsoft.trademarksearchservice.exception;

import static by.intexsoft.trademarksearchservice.utils.ConstantUtil.Exception.NO_FOUND_PATTERN;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Class<?> resourceType, String fieldName, Object fieldValue) {
        super(String.format(NO_FOUND_PATTERN, resourceType.getSimpleName(), fieldName, fieldValue));
    }
}
