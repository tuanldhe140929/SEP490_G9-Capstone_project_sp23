import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { ErrorResponse } from '../../../../DTOS/ErrorResponse';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  constructor(private http: HttpClient, private formBuilder: FormBuilder, private authService: AuthService, private router: Router) {

  }
  msg = '';
  email: String | undefined;

  resetPasswordRequestForm = this.formBuilder.group({
    "email": ['', [Validators.required, Validators.email]],
  });

  ngOnInit(): void {

  }

  onResetPasswordRequest() {
    if (this.resetPasswordRequestForm.valid) {
      if (this.resetPasswordRequestForm.value.email != null) {
        this.email = this.resetPasswordRequestForm.value.email.toString();
        this.authService.resetPasswordRequest(this.resetPasswordRequestForm.value.email.toString()).subscribe(
          (data) => {
            this.msg = "Đổi mật khẩu thành công, hãy kiểm tra hòm thư";
          },
          (error) => {
            let errorResponse: ErrorResponse = error.error;
            switch (errorResponse.status) {
              case 404:
                this.msg = "Không tìm thấy email";
                break;
              case 400:
                this.msg = "Không thể gửi email tới hòm thư";
                break;
              default:
                this.msg = "Có lỗi xảy ra";
            }
          });
      }
    }
  }

  public redirectLogin() {
    this.router.navigate(['login']);
  }

  get Form1() {
    return this.resetPasswordRequestForm.controls;
  }

}
