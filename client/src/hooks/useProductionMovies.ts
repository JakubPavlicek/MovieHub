import { useNavigate, useParams } from "react-router-dom";
import { useApi } from "@/context/ApiProvider";
import { useEffect } from "react";

export const useProductionMovies = () => {
  const { companyName } = useParams();
  const navigate = useNavigate();
  const api = useApi();

  const { data: company, isSuccess } = api.useQuery("get", "/production-companies", {
    params: {
      query: {
        limit: 1,
        name: companyName,
      },
    },
  });

  const companyId = company?.content[0]?.id;

  const { data: movies, isError } = api.useQuery(
    "get",
    `/production-companies/{companyId}/movies`,
    {
      params: {
        path: { companyId: companyId! },
      },
    },
    {
      enabled: isSuccess && company?.content?.length > 0,
    },
  );

  useEffect(() => {
    if (isError) {
      navigate("/", { replace: true });
    }
  }, [isError, navigate]);

  return { companyName, movies };
};
