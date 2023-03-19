export enum TransactionStatus{
	FAILED = "FAILED",COMPLETED="COMPLETED",CREATED="CREATED",
	CANCELED="CANCELED"
}

export enum Type{
	BUY="BUY",SELL="SELL"
}
export class Transaction{
	id:number;
	amount:number;
	status:TransactionStatus;
	type:Type;
	createdDate:Date;
	lastModified:Date;
	description:string;
	paypalId:number;
	approvalUrl:string;
	
	constructor(){
		this.id = -1;
		this.amount = -1;
		this.status = TransactionStatus.FAILED;
		this.type = Type.SELL;
		this.createdDate = new Date;
		this.lastModified = new Date;
		this.paypalId = -1;
		this.approvalUrl = '';
	}
	
	
}


