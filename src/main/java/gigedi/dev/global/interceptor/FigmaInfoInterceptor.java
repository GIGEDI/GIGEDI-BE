package gigedi.dev.global.interceptor;

import static gigedi.dev.global.common.constants.HeaderConstants.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

public class FigmaInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String figmaId = request.getHeader(FIGMA_ID_HEADER);
        String fileId = request.getHeader(FILE_ID_HEADER);

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        attributes.setAttribute(FIGMA_ID_ATTRIBUTE, figmaId, RequestAttributes.SCOPE_REQUEST);
        attributes.setAttribute(FILE_ID_ATTRIBUTE, fileId, RequestAttributes.SCOPE_REQUEST);

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        attributes.removeAttribute(FIGMA_ID_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        attributes.removeAttribute(FILE_ID_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
    }
}
