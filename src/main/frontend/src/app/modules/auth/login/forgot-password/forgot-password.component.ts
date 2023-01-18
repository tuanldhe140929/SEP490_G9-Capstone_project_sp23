import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
 	errorMessage = '';
  	email:String | undefined;
  	
  	resetPasswordRequestForm = this.formBuilder.group({
		"email":['',[Validators.required,Validators.email]],
	});
  	
	resetPasswordForm = this.formBuilder.group({
		"captcha":['',[Validators.required]],
		"newPassword":['',[Validators.required,Validators.minLength(5),Validators.maxLength(30)]]
	});
	
	get Form1(){
		return this.resetPasswordRequestForm.controls;
	}
	
	get Form(){
		return this.resetPasswordForm.controls;
	}
	constructor(private http:HttpClient, private formBuilder:FormBuilder, private authService:AuthService,  private router:Router){
		
	}
	
    ngOnInit(): void {
       
    }
    
    onResetPassword(){
		if(this.resetPasswordForm.valid && this.email!=null){
			this.authService.resetPassword(this.resetPasswordForm.value, this.email).subscribe();
		}
		
	}
	 onResetPasswordRequest(){
		if(this.resetPasswordRequestForm.valid){
			if(this.resetPasswordRequestForm.value.email!=null){
			this.email = this.resetPasswordRequestForm.value.email.toString();
			this.authService.resetPasswordRequest(this.resetPasswordRequestForm.value.email.toString()).subscribe();
			}
			
		}
		
	}
    
}
