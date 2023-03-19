import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AccountService } from 'src/app/services/account.service';
@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  @ViewChild('messageModal', { static: false }) private messageModal: any;


  message = '';
  email: String | undefined;

  resetPasswordRequestForm = this.formBuilder.group({
    "email": ['', [Validators.required, Validators.email]],
  });


  get Form1() {
    return this.resetPasswordRequestForm.controls;
  }


  constructor(private http: HttpClient,
    private formBuilder: FormBuilder,
    private accountService: AccountService,
    private router: Router,
    private modalService: NgbModal) {

  }
  openModal() {
    this.modalService.open(this.messageModal, { centered: true });
  }
  dismissError() {
    this.message = "";
    this.modalService.dismissAll();
  }
  ngOnInit(): void {

  }

  onResetPasswordRequest() {
    if (this.resetPasswordRequestForm.valid) {
      if (this.resetPasswordRequestForm.value.email != null) {
        this.email = this.resetPasswordRequestForm.value.email.toString();
        this.accountService.resetPassword(this.resetPasswordRequestForm.value.email.toString()).subscribe(
          data => {
            this.message = "Đặt lại password thành công, hãy kiểm tra email";
          },
          error => {
            this.message = "Yêu cầu chưa được thực hiện"
          });
      }
    }
  }
}
