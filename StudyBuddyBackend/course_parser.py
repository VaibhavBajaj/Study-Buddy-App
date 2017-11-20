import requests
import xml.etree.ElementTree as ET
from constants import UIUC_COURSES_API_URL


class CourseParser:
    args = list()

    def __init__(self, year, semester):
        self.args.append(year)
        self.args.append(semester)

    def __get_root(self, args):
        # Creating GET request url
        url = UIUC_COURSES_API_URL.format(args)

        xml_data = requests.get(url)
        if xml_data.status_code != 200:
            return None

        root = ET.fromstring(xml_data.content)
        return root

    @staticmethod
    def __get_text(data):
        if data is None:
            return ''
        return data.text

    def __get_data(self, root, key):
        data = list()
        if root is None:
            return data


        for item in root.iter(key):
            item_data = dict()
            item_data['id'] = item.get('id')
            item_data['name'] = self.__get_text(item)
            data.append(item_data)
        return data

    def get_section_details(self, dept_id, course_id, crn):
        # Base arguments
        args = list(self.args)

        # Adding arguments
        args.append(dept_id)
        args.append(course_id)
        args.append(crn)
        args = '/'.join(args)

        data = dict()
        root = self.__get_root(args)
        if root is None:
            return data

        data['crn'] = crn
        data['section'] = self.__get_text(root.find('sectionNumber'))
        data['course_id'] = course_id
        data['dept_id'] = dept_id
        data['start_date'] = self.__get_text(root.find('startDate'))
        data['end_date'] = self.__get_text(root.find('endDate'))

        parents = root.find('parents')
        data['dept_name'] = self.__get_text(parents.find('subject'))
        data['course_name'] = self.__get_text(parents.find('course'))
        data['term'] = self.__get_text(parents.find('term'))

        instructors = list()
        for instructor in root.iter('instructor'):
            instructors.append(instructor.text)
        data['instructors'] = instructors
        return data

    def get_item_list(self, key, *path_args):
        # Base arguments
        args = list(self.args)
        # Adding arguments
        args.extend(path_args)
        args = '/'.join(args)

        root = self.__get_root(args)
        item_list = self.__get_data(root, key)
        return item_list