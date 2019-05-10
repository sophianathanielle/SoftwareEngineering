const functions = require('firebase-functions');
const admin = require('firebase-admin');
const request = require('request-promise')
admin.initializeApp(functions.config().firebase);

exports.calculateRatings = functions.firestore.document('profile/{document_id}')
	.onUpdate((change, context) => {
		let data = change.after.data();
		let document_id = context.params.document_id;
		let rating = 0;
		let ratingCount = 0;
		let ratingsOf1 = data.ratingsOf1;
		let ratingsOf2 = data.ratingsOf2;
		let ratingsOf3 = data.ratingsOf3;
		let ratingsOf4 = data.ratingsOf4;
		let ratingsOf5 = data.ratingsOf5;
		var db = admin.firestore();
		let i;
		if(ratingsOf1 !== 0){
			for(i = 0; i < ratingsOf1; i++){
				rating +=1;
				ratingCount +=1;
			}
		}
		if(ratingsOf2 !== 0){
			for(i = 0; i < ratingsOf2; i++){
				rating +=2;
				ratingCount +=1;
			}
		}
		if(ratingsOf3 !== 0){
			for(i = 0; i < ratingsOf3; i++){
				rating +=3;
				ratingCount +=1;
			}
		}
		if(ratingsOf4 !== 0){
			for(i = 0; i < ratingsOf4; i++){
				rating +=4;
				ratingCount +=1;
			}
		}
		if(ratingsOf5 !== 0){
			for(i = 0; i < ratingsOf5; i++){
				rating +=5;
				ratingCount +=1;
			}
		}
		if(ratingCount !== 0){
			rating = rating/ratingCount;
			db.collection('profile').doc(document_id).update({rating: rating});
		}
		return true;
	});
