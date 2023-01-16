import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
	
	
	loginForm = this.formBuilder.group({
		"email": ['',[Validators.required,Validators.email]],
		"password": ['',[Validators.required,Validators.minLength(5),Validators.maxLength(30)]],
		"rememberMe":['']
	});
	
	constructor(private http:HttpClient, private formBuilder:FormBuilder, private authService:AuthService){
		
	}
	
    ngOnInit(): void {
        
    }
    
    public onLogin() {	
		if(this.loginForm.valid){
			this.authService.login(this.loginForm.value).subscribe(
				data => {console.log(data)},
				error => {}
			);
		}
	}
	
	public loginWithGoogle(){
		window.location.href = "https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri="
		+"http://localhost:4200/auth/loginWithGoogle&response_type=code&client_id=1090876037699-6spfc0lk141gc1jo3fba602913bcu4h7.apps.googleusercontent.com&approval_promt=force";
	}
}
