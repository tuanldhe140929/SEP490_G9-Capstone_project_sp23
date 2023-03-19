import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login-with-google',
  templateUrl: './login-with-google.component.html',
  styleUrls: ['./login-with-google.component.css']
})
export class LoginWithGoogleComponent implements OnInit {


  constructor(private storageService: StorageService, private route: ActivatedRoute,
   private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    var code = this.route.snapshot.queryParamMap.get('code');
    console.log(code);
    /*if (code != null) {
      this.userService.loginWithGoogle(code).subscribe(
        (response) => {
          this.router.navigate(['login']);
          this.storageService.saveUser(response.body);
          this.storageService.saveToken(response.body.accessToken);
        }
        , error => {
          console.log(error);
        }
      );
    }*/
  }
}
