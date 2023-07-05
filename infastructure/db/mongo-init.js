db.createUser(
    {
        user: "SomeUser",
        pwd: "SomePassword",
        roles: [
            {
                role: "readWrite",
                db: "searching_trademark_test_db"
            }
        ]
    }
);