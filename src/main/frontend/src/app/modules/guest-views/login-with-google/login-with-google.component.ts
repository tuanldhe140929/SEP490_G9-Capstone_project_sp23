import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login-with-google',
  templateUrl: './login-with-google.component.html',
  styleUrls: ['./login-with-google.component.css']
})
export class LoginWithGoogleComponent implements OnInit {
  message = '';
  @ViewChild('messageModal', { static: false }) private messageModal: any;
  constructor(private storageService: StorageService, private route: ActivatedRoute,
    private userService: UserService, private router: Router,
    private modalService: NgbModal
   ) { }

  ngOnInit(): void {
    var code = this.route.snapshot.queryParamMap.get('code');
    console.log(code);
    if (code != null) {
      this.userService.loginWithGoogle(code).subscribe(
        (response) => {
          this.router.navigate(['home']);
          this.storageService.saveUser(response.body);
          this.storageService.saveToken(response.body.accessToken);
        }
        , error => {
          
          if (error.status == 409 || error.status ==500) {
            this.message = 'Email đã được đăng kí hoặc không thể đăng nhập bằng token';
            this.openModal();
          }
        }
      );
    }
  }
  openModal() {
    this.modalService.open(this.messageModal, { centered: true });
  }
  dismissError() {
    this.message = "";
    this.modalService.dismissAll();
  }
}
