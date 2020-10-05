# 3717Project

### Team 
- Gyeongmin Lee (A01051107)
- Daniel Luong (A00877970)
- Lena Kwan (A01020370)

### Running the App
1. Clone repository and open folder "ClearCouncil" with android studio
1. Click "Sync Project with Gradle Files"
1. Run in android studio

### Main files
- `DashboardFragment.java` - Fragment responsible for calling OpenData API and displaying the votes
- `VoteDetailActivity.java` - Fragment responsible for showing vote details
- `Vote.java` - Core data class
- `VoteAdapter.java` - Converts list of votes into list of cardViews
- `CommentFragment.java`, `CommentDatabase.java` - Connects to firebase realtime db to implement comments
- `NetworkUtils.java` - Build url to access OpenData API
- `JSONParseUtils.java` - Parse JSON response from API and return `Vote` and `CouncilMember`
