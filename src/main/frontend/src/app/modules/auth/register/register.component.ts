import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { data } from 'jquery';
import { AuthResponse } from 'src/app/DTOS/AuthResponse';
import { AuthService } from 'src/app/services/auth.service';
import { StorageService } from 'src/app/services/storage.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorResponse } from '../../../DTOS/ErrorResponse';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  @ViewChild('content', { static: false }) private content: any;
  username = "";
  errors: string[] = [];
  registerForm = this.formBuilder.group({
    "email": ['', [Validators.required, Validators.email]],
    "username": ['', [Validators.required, Validators.minLength(3), Validators.maxLength(10),
    this.noWhitespaceValidator]],
    "password": ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]],
    "repeat_password": ['', [Validators.required]]
  });

  public noWhitespaceValidator(control: FormControl) {
    const isWhitespace = (control.value || '').trim().includes(' ');
    const isValid = !isWhitespace;
    return isValid ? null : { 'whitespace': true };
  }

  onPasswordMatch() {
    if (this.confirm_password.value == this.password.value) {
      return true;
    } else {
      return false;
    }
  }

  public onUsernameChange($event: any) {
    if ($event.keyCode === 32) {
      $event.preventDefault();
    }
    this.username = this.username.toLowerCase();
    this.registerForm.controls.username.setValue(this.username);
  }


  constructor(private http: HttpClient, private formBuilder: FormBuilder, private authService: AuthService,
    private router: Router, private storageService: StorageService, private modalService: NgbModal) { }

  ngOnInit(): void { }

  public onRegister() {

    this.registerForm.controls.username.setValue(this.username);
    if (this.registerForm.valid) {
      this.authService.register(this.registerForm.value).subscribe(
        data => {
          console.log(data);
          this.errors.push("Đăng kí thành công, đang gửi email xác thực tới địa chỉ email của bạn");
          this.storageService.saveRegisteredEmail(data.email);
          this.sendVerifyEmail(data.email);
          this.openVerticallyCentered();
        },
        error => {
          let errorResponse: ErrorResponse = error.error;
          if (error.status === 409) {

            if (errorResponse.messages[0].includes('email')) {
              this.errors.push('Email đã tồn tại');
            }
            if (errorResponse.messages[0].includes('username')) {
              this.errors.push('Username đã tồn tại');
            }

          }
          this.openVerticallyCentered();
        }
      );
    }
  }

  public sendVerifyEmail(email: string) {
    this.authService.sendVerifyEmail(email).subscribe(
      (data) => {
        if (data == true) {
          this.errors.push('Hãy kiểm tra hòm thư');
          this.openVerticallyCentered();
        }
      },
      (error) => {
        this.errors.push('Có lỗi khi gửi mail');
        this.openVerticallyCentered();
      }
    )
  }

    openVerticallyCentered() {
    this.modalService.open(this.content, { centered: true });
  }

  dismissError() {
    this.errors = [];
    this.modalService.dismissAll();
  }

  public redirectLogin() {
    this.router.navigate(['login']);
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
}
