package beertag.security;

import beertag.exception.AccountNotActivatedException;
import beertag.exception.EntityNotFoundException;
import beertag.models.User;
import beertag.repositories.contracts.UserRepository;
import beertag.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserService userService;

    public CustomAuthenticationProvider() {
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        try {
            final User user = userService.getUserByUsername(auth.getName());
            // to verify verification code
//        if (user.isUsing2FA()) {
//            final String verificationCode = ((CustomWebAuthenticationDetails) auth.getDetails()).getVerificationCode();
//            final Totp totp = new Totp(user.getSecret());
//            if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
//                throw new BadCredentialsException("Invalid verification code");
//            }
//
//        }
            final Authentication result = super.authenticate(auth);
            return new UsernamePasswordAuthenticationToken(user, result.getCredentials(), result.getAuthorities());
        } catch (final EntityNotFoundException e) {
            throw new BadCredentialsException("Invalid username or password");
        }catch (AccountNotActivatedException e){
            throw new DisabledException(e.getMessage());
        }
    }

    private boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (final NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}