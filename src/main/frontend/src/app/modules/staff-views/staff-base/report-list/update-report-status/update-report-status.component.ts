import { Component, Inject , OnInit} from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { Report } from 'src/app/DTOS/Report';
import { User } from 'src/app/DTOS/User';
import { ViolationType } from 'src/app/DTOS/ViolationType';
import { ProductService } from 'src/app/services/product.service';
import { ReportService } from 'src/app/services/report.service';
import { UserService } from 'src/app/services/user.service';
import { ViolationTypeService } from 'src/app/services/violation-type.service';

interface ReportEntity{
  username: string,
  description: string,
  createdDate: string
}

@Component({
  selector: 'app-update-report-status',
  templateUrl: './update-report-status.component.html',
  styleUrls: ['./update-report-status.component.css']
})
export class UpdateReportStatusComponent implements OnInit{

  reportList: Report[] = [];
  userList: User[] = [];
  violationTypeList: ViolationType[] = []

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: {productId: number}, 
    private productService: ProductService,
    private toastr: ToastrService,
    private reportService: ReportService,
    private userService: UserService,
    private violationTypeService: ViolationTypeService){

    }

    ngOnInit(): void {
      this.reportService.getByStatus("PENDING").subscribe( reports => {
        this.reportList = reports;
        this.userService.getAllUsers().subscribe(users => {
          this.userList = users;
          const reportEntities = this.reportList.map(report => {
            const user = this.userList.find(u => u.id === report.product.id);
            return{
              username: user?.username,
              description: report.description,
              createdDate: report.created_date
            } as ReportEntity
            
          })
          console.log(reportEntities);
          // this.violationTypeService.getAllTypes().subscribe(viotypes => {
          //   this.violationTypeList = viotypes;
          //   const reportEntities = this.reportList.map(report => {
          //     const user = this.userList.find(u => u.id === report.product.id);
          //     const viotype = this.violationTypeList.find(vt => vt.id === report.violation_type.id);
          //     return{
          //       username: user?.username,
          //       violationTypeName: viotype?.name,
          //       description: report.description,
          //       createdDate: report.created_date
          //     } as ReportEntity
          //   })
          //   console.log(reportEntities);
          // })
        })
      })


    //       this.reportService.getByStatus("PENDING").subscribe (reports => {
    //   this.reports = reports;
    //   console.log(reports);
    //   this.userService.getAllUsers().subscribe( users => {
    //     this.users = users;
    //     this.productService.getAllProductsLatestVers().subscribe (products => {
    //       this.products = products;
    //       this.reportEntities = this.reports.map(report => {
    //         const product = this.products.find(p => {p.id === report.product.id; console.log(p.id+" "+report.product.id)});
    //         const user = this.users.find(u => {u.id === report.user.id; console.log(u.id+" "+report.user.id)});
    //         return{
    //           productId: report.product.id,
    //           productName: report.product.name,
    //           productVersion: report.product.activeVersion,
    //           userId: report.user.id,
    //           userName: report.user.username,
    //           reportedDate: report.created_date,
    //           reportedDescription: report.description,
    //           reportedViolationType: report.violation_type
    //         } as ReportEntity;
            
    //       })
    //       console.log(this.reportEntities);
    //       this.dataSource = new MatTableDataSource(this.reportEntities);
    //     this.reportList = this.reportEntities;
    //     this.dataSource.paginator = this.paginator;
    //     this.dataSource.sort = this.sort;
    //     })
        
    //   });
      
    // });
    }

    // onApprove(status: string){
    //   this.productService.updateApprovalStatus(this.data.productId, this.data.version, status).subscribe(
    //     data => {
    //       console.log(data);
    //       if(status=='APPROVED'){
    //         this.toastr.success(this.data.productName+' phiên bản '+this.data.version,'Duyệt sản phẩm thành công')
    //       }else{
    //         this.toastr.success(this.data.productName+' phiên bản '+this.data.version,'Từ chối sản phẩm thành công')
    //       }
    //     }
    //   )
    // }
}
