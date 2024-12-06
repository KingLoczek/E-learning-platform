package edu.sigmaportal.platform.service.impl;

import edu.sigmaportal.platform.model.PermissionModel;
import edu.sigmaportal.platform.model.UserModel;
import edu.sigmaportal.platform.model.UserTypeModel;
import edu.sigmaportal.platform.repository.PermissionRepository;
import edu.sigmaportal.platform.repository.UserRepository;
import edu.sigmaportal.platform.security.UserTypeAuthority;
import edu.sigmaportal.platform.service.UserTypeService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository users;
    private final PermissionRepository permissions;
    private final UserTypeService userTypes;

    public UserDetailsServiceImpl(UserRepository users, PermissionRepository permissions, UserTypeService userTypes) {
        this.users = users;
        this.permissions = permissions;
        this.userTypes = userTypes;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = users.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        List<PermissionModel> perms = permissions.findAllByUserTypeId(user.userTypeId());
        UserTypeModel type = userTypes.getUsersType(user);
        List<GrantedAuthority> authorities = Stream.concat(perms.stream(), Stream.of(new UserTypeAuthority(type))).toList();
        return new User(user.email(), user.password(), true, true, true, true, authorities);
    }
}
