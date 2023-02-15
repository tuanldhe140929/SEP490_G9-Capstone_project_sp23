import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { StorageService } from 'src/app/services/storage.service';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  errorMessage = '';
  role: string = '';

  loginForm = this.formBuilder.group({
    "email": ['', [Validators.required, Validators.email]],
    "password": ['', [Validators.required, Validators.minLength(8), Validators.maxLength(30)]],
    "rememberMe": ['']
  });

  get Form() {
    return this.loginForm.controls;
  }
  constructor(private http: HttpClient, private formBuilder: FormBuilder, private authService: AuthService, private storageService: StorageService, private router: Router) {

  }

  ngOnInit(): void {
    if (this.storageService.getToken()) {
      this.role = this.storageService.getAuthResponse().role;
    }
  }

  public onLogin() {

    if (this.loginForm.valid) {
    if(this.loginForm.controls.email.value!=null){
		 this.loginForm.controls.email.setValue(this.loginForm.controls.email.value.trim());
	}
      this.authService.login(this.loginForm.value).subscribe(

        response => {
			console.log(response.body);
          AuthService.isLoggedIn = true;
          this.authService.authResponse = response.body;
          this.storageService.saveUser(response.body);
          this.storageService.saveToken(response.body.accessToken);
        },
        err => {
			console.log(err);
          if (err.status === 500) {
            this.storageService.clearStorage();
            this.errorMessage = 'Sai email hoặc mật khẩu';
          }
        })

    } else {
      Object.keys(this.loginForm.controls).forEach(field => { // {1}
        const control = this.loginForm.get(field);
        if (control != null)          // {2}
          control.markAsTouched({ onlySelf: true });       // {3}
      });
    }
  }


  public loginWithGoogle() {
    window.location.href = "https://accounts.google.com/o/oauth2/auth?scope=email&redirect_uri="
      + "http://localhost:4200/auth/loginWithGoogle&response_type=code&client_id=1090876037699-6spfc0lk141gc1jo3fba602913bcu4h7.apps.googleusercontent.com&approval_promt=force";
  }
  public logOut() {
    console.log('loginout');
    this.authService.logout().subscribe();
    this.router.navigate(['login']);
  }

  public redirectForgotPassword() {
    this.router.navigate(['forgotPassword']);
  }

  public redirectRegister() {
    this.router.navigate(['register']);
  }
}
