package com.sl.web.server.controller

import com.sl.web.server.entity.ValidationCode
import com.sl.web.server.service.ValidationCodeService
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ApplyValidationCodeController:BasicController() {

    @Autowired
    lateinit var validationCodeService:ValidationCodeService

    @GetMapping("/apple_code")
    suspend fun applyCode(@RequestParam(name = "user_id") user_id:String){
        withContext(coroutineContext) {
            var bean = validationCodeService.query(user_id)
            if (bean == null) {
                bean = ValidationCode()
                bean.userId = user_id

            }else{
                val time = Date(bean.lastApplyTime).time
                if ((System.currentTimeMillis() - time)>=60) {

                }
            }
        }
    }
}