#include "PhoneBook.h"


// Set default value static members
PhoneBook* PhoneBook::ptInstance = nullptr;

PhoneBookDestroyer PhoneBook::destroyer;


// Auxiliary class methods implementations
PhoneBookDestroyer::PhoneBookDestroyer() {
    phoneBookInstance = nullptr;
}


PhoneBookDestroyer::~PhoneBookDestroyer() {
    delete phoneBookInstance;
}


void PhoneBookDestroyer::initialize( PhoneBook* ptPhoneBook ) {
    phoneBookInstance = ptPhoneBook;
}


// PhoneBook Class methods
PhoneBook* PhoneBook::getInstance() {
    if (ptInstance == nullptr) {
        ptInstance = new PhoneBook();
        destroyer.initialize(ptInstance);
    }

    return (ptInstance);
}


PhoneBook::PhoneBook()
{
    // 21 contacts
    contactMap = {
            {"Zermelo Fraenkel",      "+7 902 585 00 55"},
            {"Benedict Austin",       "+7 901 343 22 34"},
            {"Anna Brown",            "+7 900 159 64 87"},
            {"Charlie Chaplin",       "+1 475 654 87 21"},
            {"Ludwig Wittgenstein",   "+4 147 252 39 97"},
            {"Johan A.",              "+4 987 873 87 64"},
            {"Jonathan Whitley",      "+3 123 454 21 32"},
            {"Edward (guitarist)",    "+7 456 321 32 21"},
            {"Jack B.",               "+7 789 123 45 67"},
            {"BOSS",                  "+3 321 632 12 34"},
            {"Alexander The Great",   "+4 654 091 67 89"},
            {"Mom",                   "+5 987 651 12 56"},
            {"SECOND WORK",           "+6 712 331 21 56"},
            {"Honey :3",              "+7 098 764 63 90"},
            {"Robert Brandom",        "+7 891 241 24 23"},
            {"Maria J.",              "+7 321 123 42 29"},
            {"Edmund Husserl",        "+9 654 124 12 41"},
            {"Hegel",                 "+8 987 123 12 69"},
            {"C++ courses",           "+7 123 456 72 89"},
            {"WORK",                  "+7 123 456 78 39"},
            {"DO NOT ANSWER",         "+7 031 544 88 00"}
    };
}


std::string PhoneBook::sendContactsList() const
{
    json contactList;
    json contact;

    for (const auto& n : contactMap)
    {
        contact["Name"] = n.first;
        contact["Number"] = n.second;

        contactList.push_back(contact);
        contact.clear();
    }

    json result;
    result["contactList"] = contactList;
    std::string json_str = result.dump();

    return json_str;
}


std::string PhoneBook::findContactByName(const std::string& name) const
{
    json contactList;
    json contact;

    for (const auto& n : contactMap)
    {
        if (n.first.find(name) != std::string::npos)
        {
            contact["Name"] = n.first;
            contact["Number"] = n.second;
            contactList.push_back(contact);
            contact.clear();
        }
    }

    if (contactList.empty())
        return "None";
    else
    {
        json result;
        result["contactList"] = contactList;
        std::string s = result.dump();
        return s;
    }
}


void PhoneBook::addContact(const std::string& jsonStr)
{
    json request = json::parse(jsonStr);
    contactMap[request["Name"]] = request["Number"];
}
