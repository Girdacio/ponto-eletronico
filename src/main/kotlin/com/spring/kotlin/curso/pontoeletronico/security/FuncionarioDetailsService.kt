package com.spring.kotlin.curso.pontoeletronico.security

import com.spring.kotlin.curso.pontoeletronico.documents.Funcionario
import com.spring.kotlin.curso.pontoeletronico.services.FuncionarioService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class FuncionarioDetailsService(val funcionarioService: FuncionarioService) : UserDetailsService {

    override fun loadUserByUsername(userName: String?): UserDetails {
        if (userName != null) {
            val funcionario: Funcionario? = funcionarioService.buscarPorEmail(userName)
            if (funcionario != null) {
                return FuncionarioPrincipal(funcionario)
            }
        }
        throw UsernameNotFoundException(userName)
    }
}