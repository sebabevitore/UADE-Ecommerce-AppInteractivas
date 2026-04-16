package com.uade.ecommerce.service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.ecommerce.dto.UsuarioRegisterDTO;
import com.uade.ecommerce.dto.LoginRequestDTO;
import com.uade.ecommerce.exception.EmailException;
import com.uade.ecommerce.model.Role;
import com.uade.ecommerce.model.Usuario;
import com.uade.ecommerce.repository.UsuarioRepository;
import com.uade.ecommerce.security.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    
    public String register(UsuarioRegisterDTO request) {


        //    * FLUJO DE EJECUCIÓN:
        //    * 1. Valida que el email no esté duplicado en el sistema
        //    * 2. Construye una nueva entidad Usuario con el patrón Builder
        //    * 3. Encripta la contraseña usando BCrypt (algoritmo seguro resistente a ataques de fuerza bruta)
        //    * 4. Asigna automáticamente el rol de usuario básico (USER)
        //    * 5. Persiste el usuario en la base de datos dentro de una transacción
        //    * 6. Retorna un mensaje de confirmación
        if(usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailException();
        }


        //BUILDER
        Usuario usuario = Usuario.builder()
                // 2.1) Asigna el nombre completo del usuario desde el request
                .nombre(request.getNombre())
                // 2.2) Asigna el apellido del usuario desde el request
                .apellido(request.getApellido())
                // 2.3) Asigna el email del usuario (ya validado como único en PASO 1)
                .email(request.getEmail())
                // 2.4) ENCRIPTACIÓN DE LA CONTRASEÑA (PASO CRÍTICO DE SEGURIDAD)
                //      Se utiliza passwordEncoder (PasswordEncoder de Spring Security) 
                //      para codificar la contraseña usando el algoritmo BCrypt
                //      IMPORTANTE: La contraseña NUNCA se almacena en texto plano en la BD
                //      Características de seguridad de BCrypt:
                //      - Resistente a ataques de fuerza bruta (computacionalmente costoso)
                //      - El mismo password se codifica diferente en cada llamada
                //      - Al verificar en login, compara el hash almacenado con el nuevo hash generado
                .password(passwordEncoder.encode(request.getPassword()))
                // 2.5) Asigna el rol de usuario a todos los registros nuevos
                //      Todos los usuarios nuevos tienen rol USER (permisos limitados)
                //      Solo administradores pueden asignar roles especiales (ADMIN, MODERATOR, etc.)
                //      Esto sigue el principio de "least privilege" (menor nivel de privilegios)
                .role(Role.USER)
                .fechaNacimiento(LocalDate.parse(request.getFechaNacimiento()))
                .sexo(request.getSexo())
                // 2.6) Finaliza la construcción y retorna la instancia Usuario 
                //      con todos los campos configurados y listos para usar
                .build();

        // GUARDADO
        usuarioRepository.save(usuario);

        return "Usuario registrado exitosamente";
    }

        /**
     * Método que autentica un usuario existente y genera un token JWT.
     * 
     * Este método implementa el flujo completo de autenticación:
     * 1. Valida las credenciales (email y contraseña) contra la base de datos
     * 2. Verifica que el usuario existe y que la contraseña es correcta
     * 3. Extrae los roles/permisos del usuario autenticado
     * 4. Genera un token JWT con la información de autenticación
     * 5. Retorna el token al cliente para futuras solicitudes autenticadas
     * 
     * FLUJO DE SEGURIDAD:
     * - Se utiliza Spring Security AuthenticationManager para validar credenciales
     * - Las contraseñas se comparan de manera segura usando BCrypt
     * - El token JWT se genera con email y roles del usuario
     * - El cliente debe incluir este token en el header Authorization de futuras solicitudes
     * 
     * @param request objeto LoginRequest que contiene:
     *                - email: identificador único del usuario
     *                - password: contraseña en texto plano (será encriptada internamente para validación)
     * @return token JWT (JSON Web Token) que el cliente debe usar para autenticarse en solicitudes futuras
     * @throws UsernameNotFoundException si el usuario (email) no existe en la base de datos
     * @throws BadCredentialsException si la contraseña proporcionada es incorrecta
     * @throws NoSuchElementException si no se encuentra el usuario después de la autenticación exitosa
     */

    public String authenticate(LoginRequestDTO request){

        // ==================== PASO 1: VALIDACIÓN DE CREDENCIALES ====================
        //esto guarda las credenciales para que las lea el AuthenticationManager
        UsernamePasswordAuthenticationToken credenciales = new UsernamePasswordAuthenticationToken(
            request.getEmail(), 
            request.getPassword()
        ); 
        // El AuthenticationManager se encarga de validar las credenciales contra la base de datos
        authenticationManager.authenticate(credenciales); // esto lanza excepciones si algo esta mal. 

        // ==================== PASO 2: OBTENER USUARIO AUTENTICADO ====================
        Usuario user = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

        // ==================== PASO 3: EXTRAER ROLES/PERMISOS ====================
                // Obtiene la lista de roles/permisos del usuario autenticado
        //
        // user.getAuthorities():
        // - Retorna una Collection<? extends GrantedAuthority>
        // - GrantedAuthority representa un permiso o rol del usuario
        // - Ejemplo: ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR
        //
        // Flujo de transformación (Stream API):
        // 1. user.getAuthorities().stream() - convierte la colección en un stream
        // 2. .map(grantedAuthority -> grantedAuthority.getAuthority()) - extrae el nombre del rol (String)
        //    * Transforma cada GrantedAuthority en su representación como String
        //    * Ejemplo: [GrantedAuthority("ROLE_USER")] -> ["ROLE_USER"]
        // 3. .collect(Collectors.toSet()) - recolecta los resultados en un Set<String>
        //    * Set evita duplicados (aunque normalmente un usuario no tiene roles duplicados)
        //    * Ejemplo salida: {"ROLE_USER", "ROLE_ADMIN"}
        Set<String> roles = user.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toSet());
        
        // ==================== PASO 4: GENERACIÓN DEL TOKEN JWT ====================
        // Genera un token JWT (JSON Web Token) que será enviado al cliente
        //
        // JWT (JSON Web Token):
        // - Formato estándar para tokens de autenticación
        // - Estructura: [header].[payload].[signature]
        // - Es un token autofirmado (contiene su propia validación mediante firma)
        // - No se requiere consultar BD en cada solicitud para validarlo (validación local)
        // - Contiene información encriptada sobre el usuario (email, roles)
        // - Tiene expiration time (vencimiento después de cierto tiempo)
        // - El cliente lo debe incluir en el header Authorization para futuras solicitudes
        //
        // jwtUtil.generateToken(email, roles):
        // - Crea un token JWT con el email y los roles del usuario
        // - Firma el token con una clave secreta definida en la aplicación
        // - El servidor podrá verificar este token en futuras solicitudes sin consultar BD
        // - Solo tokens con firma válida serán aceptados (previene manipulación)
        return jwtUtil.generateToken(user.getEmail(), roles);
    }
    

}
