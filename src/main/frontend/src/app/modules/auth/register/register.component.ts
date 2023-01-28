import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  username = "";
  message = "";

  registerForm = this.formBuilder.group({
    "email": ['', [Validators.required, Validators.email]],
    "username": ['', [Validators.required, Validators.minLength(3), Validators.maxLength(10)]],
    "password": ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]],
    "repeat_password": ['', [Validators.required]]
  });

  onPasswordMatch() {

    if (this.confirm_password.value == this.password.value) {
      return true;
    } else {
      return false;
    }
  }

  get Form() {
    return this.registerForm.controls;
  }
  get password(): AbstractControl {
    return this.registerForm.controls['password'];
  }

  get confirm_password(): AbstractControl {
    return this.registerForm.controls['repeat_password'];
  }

  public redirectLogin() {
    this.router.navigate(['login']);
  }

  public onUsernameChange($event: any) {
    this.registerForm.controls.username.setValue(this.username);
  }


  constructor(private http: HttpClient, private formBuilder: FormBuilder, private authService: AuthService,
    private router: Router) { }

  ngOnInit(): void { }

  public onRegister() {
    this.registerForm.controls.username.setValue(this.username);
    console.log(this.registerForm.invalid);
    if (this.registerForm.valid) {
      
      this.authService.register(this.registerForm.value).subscribe(
        data => {
          console.log(data)
          this.message = "Dang ki thanh cong";
        },
        error => {
          if (error.status === 409) {
            if (error.error.messages[0].includes(this.registerForm.controls.username.value)) {
              this.message = "Trung ten dang nhap";
            } else {
              this.message = "Trung email"
            }
          }
        }
      );
    }
  }
}
