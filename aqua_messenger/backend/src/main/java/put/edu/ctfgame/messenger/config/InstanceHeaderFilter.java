package put.edu.ctfgame.messenger.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InstanceHeaderFilter extends OncePerRequestFilter {

    @Value("${INSTANCE_ID:instance-id}")
    private String instanceId;

    public InstanceHeaderFilter(@Value("${INSTANCE_ID:instance-id}") String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        String customHeader = request.getHeader("Instance-Id");

        if (customHeader == null || !customHeader.equals(instanceId)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid Instance-Id header");
            return;
        }

        filterChain.doFilter(request, response);
    }
}